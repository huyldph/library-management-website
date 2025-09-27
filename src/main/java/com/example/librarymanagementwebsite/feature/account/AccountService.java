package com.example.librarymanagementwebsite.feature.account;

import com.example.librarymanagementwebsite.feature.account.dto.RegisterRequest;
import com.example.librarymanagementwebsite.feature.account.dto.RegisterResponse;
import com.example.librarymanagementwebsite.feature.account.mapper.UserMapper;
import com.example.librarymanagementwebsite.constant.PredefinedRole;
import com.example.librarymanagementwebsite.exception.AppException;
import com.example.librarymanagementwebsite.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    public RegisterResponse createAccount(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Optional<Role> role = roleRepository.findByRoleName(PredefinedRole.STAFF_ROLE);
        if (role.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role.get());

        return userMapper.toResponse(userRepository.save(user));
    }
}

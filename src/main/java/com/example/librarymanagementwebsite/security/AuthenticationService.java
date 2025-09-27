package com.example.librarymanagementwebsite.security;

import com.example.librarymanagementwebsite.feature.account.User;
import com.example.librarymanagementwebsite.feature.account.UserRepository;
import com.example.librarymanagementwebsite.exception.AppException;
import com.example.librarymanagementwebsite.exception.ErrorCode;
import com.example.librarymanagementwebsite.security.dto.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception){
            log.info("Token already expired");
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Tạo verifier để kiểm tra chữ ký JWT, sử dụng cùng secret key đã ký token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Parse chuỗi token (string) thành đối tượng SignedJWT để có thể đọc claims bên trong
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Xác định thời điểm hết hạn của token
        Date expirationTime = (isRefresh)
                ? new Date( // Nếu là token refresh → cho phép tồn tại lâu hơn (issueTime + REFRESHABLE_DURATION)
                signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime(); // Nếu là access token thường → dùng expiration (exp) trong token

        // Kiểm tra chữ ký số của token có hợp lệ không (có bị giả mạo không)
        var verified = signedJWT.verify(verifier);

        // Nếu chữ ký không đúng hoặc token đã hết hạn → throw lỗi UNAUTHENTICATED
        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Kiểm tra token có bị vô hiệu hóa (logout/revoke) chưa
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getSubject())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Nếu vượt qua tất cả kiểm tra → token hợp lệ, trả về đối tượng SignedJWT để lấy claims (userId, role, scope, …)
        return signedJWT;
    }

    //    Xác thực user và trả về token nếu thành công
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

    //Tạo token cho user
    private String generateToken(User user) {
        // 1. Tạo header của JWT, quy định thuật toán ký là HS512 (HMAC với SHA-512)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // 2. Xây dựng phần payload (claims) của JWT
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserId()) // định danh người dùng (userId), dùng để biết token thuộc về ai
                .issuer("https://example.com") // ai phát hành token này (issuer)
                .issueTime(new Date()) // thời điểm phát hành token
                .expirationTime(new Date(     // thời điểm hết hạn (issueTime + VALID_DURATION giây)
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString()) // gán một ID ngẫu nhiên cho token (để phân biệt nếu cần revoke)
                .claim("scope", buildScope(user))    // thêm claim "scope" chứa role/quyền hạn của user
                .build();

        // 3. Đóng gói payload (claims) vào đối tượng Payload
        Payload payload = new Payload(claimsSet.toJSONObject());

        // 4. Kết hợp header + payload để tạo ra JWSObject
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // 5. Ký token bằng secret key với thuật toán HS512
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // 6. Chuyển JWT thành chuỗi (string) để trả về cho client
            return jwsObject.serialize();

        } catch (JOSEException e) {
            // Nếu có lỗi khi ký token thì log ra và throw RuntimeException
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        stringJoiner.add("ROLE_" + user.getRole().getRoleName());

        return stringJoiner.toString();
    }
}

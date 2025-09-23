package com.example.librarymanagementwebsite.security;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Giai mã token để kiểm tra tính hợp lệ
            SignedJWT signedJWT = SignedJWT.parse(token);

            return new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(), // Thời gian phát hành token
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), // Thời gian hết hạn token
                    signedJWT.getHeader().toJSONObject(), // Header của token
                    signedJWT.getJWTClaimsSet().getClaims() // Claims của token
            );
        } catch (Exception e) {
            throw new JwtException("Invalid token");
        }
    }
}

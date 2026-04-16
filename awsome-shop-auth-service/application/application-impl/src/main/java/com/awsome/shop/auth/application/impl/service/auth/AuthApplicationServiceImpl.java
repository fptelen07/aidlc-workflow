package com.awsome.shop.auth.application.impl.service.auth;

import com.awsome.shop.auth.application.api.dto.auth.UserDTO;
import com.awsome.shop.auth.application.api.dto.auth.request.*;
import com.awsome.shop.auth.application.api.dto.auth.response.*;
import com.awsome.shop.auth.application.api.service.auth.AuthApplicationService;
import com.awsome.shop.auth.common.exception.BusinessException;
import com.awsome.shop.auth.domain.model.auth.TokenClaims;
import com.awsome.shop.auth.domain.model.user.UserEntity;
import com.awsome.shop.auth.domain.service.user.UserDomainService;
import com.awsome.shop.auth.infrastructure.security.api.service.PasswordHashService;
import com.awsome.shop.auth.infrastructure.security.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 认证应用服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthApplicationServiceImpl implements AuthApplicationService {

    private final UserDomainService userDomainService;
    private final TokenService tokenService;
    private final PasswordHashService passwordHashService;

    @Override
    public void register(RegisterRequest req) {
        String hash = passwordHashService.hash(req.getPassword());
        userDomainService.register(req.getUsername(), hash, req.getDisplayName());
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        UserEntity user = userDomainService.authenticate(req.getUsername(), req.getPassword());
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = tokenService.generateRefreshToken(user.getId());
        return new LoginResponse(accessToken, refreshToken, toDTO(user));
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest req) {
        TokenClaims claims;
        try {
            claims = tokenService.validateToken(req.getRefreshToken());
        } catch (Exception e) {
            throw new BusinessException("AUTH_002", "Token 已过期，请重新登录");
        }
        if (!"refresh".equals(claims.getType())) {
            throw new BusinessException("AUTH_003", "无效的 Token");
        }
        UserEntity user = userDomainService.findById(claims.getUserId());
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        return new TokenResponse(accessToken);
    }

    @Override
    public ValidateResponse validateToken(ValidateRequest req) {
        try {
            TokenClaims claims = tokenService.validateToken(req.getToken());
            return ValidateResponse.success(claims.getUserId(), claims.getRole());
        } catch (Exception e) {
            return ValidateResponse.fail(e.getMessage());
        }
    }

    @Override
    public UserDTO getCurrentUser(Long operatorId) {
        UserEntity user = userDomainService.findById(operatorId);
        return toDTO(user);
    }

    private UserDTO toDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(user.getDisplayName());
        dto.setRole(user.getRole());
        dto.setAvatar(user.getAvatar());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}

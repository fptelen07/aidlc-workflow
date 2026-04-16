package com.awsome.shop.auth.facade.http.controller;

import com.awsome.shop.auth.application.api.dto.auth.UserDTO;
import com.awsome.shop.auth.application.api.dto.auth.request.*;
import com.awsome.shop.auth.application.api.dto.auth.response.*;
import com.awsome.shop.auth.application.api.service.auth.AuthApplicationService;
import com.awsome.shop.auth.facade.http.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证服务", description = "用户注册、登录、Token 管理")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @Operation(summary = "用户注册")
    @PostMapping("/api/v1/public/auth/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest req) {
        authApplicationService.register(req);
        return Result.success();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/api/v1/public/auth/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return Result.success(authApplicationService.login(req));
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/api/v1/public/auth/refresh")
    public Result<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest req) {
        return Result.success(authApplicationService.refreshToken(req));
    }

    @Operation(summary = "验证 Token（内部接口，Gateway 专用）")
    @PostMapping("/api/v1/internal/auth/validate")
    public java.util.Map<String, Object> validateToken(@Valid @RequestBody ValidateRequest req) {
        ValidateResponse resp = authApplicationService.validateToken(req);
        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("success", resp.isValid());
        result.put("operatorId", resp.getUserId() != null ? String.valueOf(resp.getUserId()) : null);
        result.put("message", resp.getMessage());
        return result;
    }

    @Operation(summary = "获取当前用户信息")
    @PostMapping("/api/v1/auth/me")
    public Result<UserDTO> getCurrentUser(@RequestBody GetCurrentUserRequest req) {
        return Result.success(authApplicationService.getCurrentUser(req.getOperatorId()));
    }

    @Operation(summary = "更新个人信息")
    @PostMapping("/api/v1/auth/profile/update")
    public Result<UserDTO> updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
        return Result.success(authApplicationService.updateProfile(req));
    }
}

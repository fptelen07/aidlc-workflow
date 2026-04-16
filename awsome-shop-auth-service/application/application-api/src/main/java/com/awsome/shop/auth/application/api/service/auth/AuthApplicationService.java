package com.awsome.shop.auth.application.api.service.auth;

import com.awsome.shop.auth.application.api.dto.auth.UserDTO;
import com.awsome.shop.auth.application.api.dto.auth.request.*;
import com.awsome.shop.auth.application.api.dto.auth.response.*;
import com.awsome.shop.auth.common.dto.PageResult;

/**
 * 认证应用服务接口
 */
public interface AuthApplicationService {

    void register(RegisterRequest req);

    LoginResponse login(LoginRequest req);

    TokenResponse refreshToken(RefreshTokenRequest req);

    ValidateResponse validateToken(ValidateRequest req);

    UserDTO getCurrentUser(Long operatorId);

    UserDTO updateProfile(UpdateProfileRequest req);

    PageResult<UserDTO> listUsers(int page, int size, String keyword);
}

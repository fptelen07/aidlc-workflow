package com.awsome.shop.auth.application.api.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResponse {
    private boolean valid;
    private Long userId;
    private String role;
    private String message;

    public static ValidateResponse success(Long userId, String role) {
        return new ValidateResponse(true, userId, role, null);
    }

    public static ValidateResponse fail(String message) {
        return new ValidateResponse(false, null, null, message);
    }
}

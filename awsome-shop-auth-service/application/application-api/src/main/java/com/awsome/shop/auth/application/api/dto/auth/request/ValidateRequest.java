package com.awsome.shop.auth.application.api.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateRequest {
    @NotBlank(message = "token 不能为空")
    private String token;
}

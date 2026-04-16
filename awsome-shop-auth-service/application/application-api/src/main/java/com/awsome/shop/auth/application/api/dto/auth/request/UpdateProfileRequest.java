package com.awsome.shop.auth.application.api.dto.auth.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String displayName;
    private Long operatorId;
}

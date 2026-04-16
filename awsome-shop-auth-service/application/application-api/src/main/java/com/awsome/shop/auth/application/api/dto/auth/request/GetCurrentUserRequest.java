package com.awsome.shop.auth.application.api.dto.auth.request;

import lombok.Data;

@Data
public class GetCurrentUserRequest {
    private Long operatorId;
}

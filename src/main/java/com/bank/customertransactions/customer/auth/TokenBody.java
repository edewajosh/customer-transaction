package com.bank.customertransactions.customer.auth;

import lombok.Builder;

@Builder
public record TokenBody(String expiry_time, String bearer_token, String token_type) {
}

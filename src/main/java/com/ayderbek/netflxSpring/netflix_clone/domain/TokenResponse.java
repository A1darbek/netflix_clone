package com.ayderbek.netflxSpring.netflix_clone.domain;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
}

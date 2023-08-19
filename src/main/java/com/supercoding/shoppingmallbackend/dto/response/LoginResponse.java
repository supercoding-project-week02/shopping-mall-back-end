package com.supercoding.shoppingmallbackend.dto.response;

import com.supercoding.shoppingmallbackend.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class LoginResponse {
    private String email;
    private String token;
    private String role;
    private String profileUrl;

    public static LoginResponse from(Profile profile, String token){
        log.info("imageProfile: {}", profile.getImageUrl());
        return new LoginResponse(
                profile.getEmail(),
                token,
                profile.getRole().toString(),
                profile.getImageUrl()
        );
    }
}

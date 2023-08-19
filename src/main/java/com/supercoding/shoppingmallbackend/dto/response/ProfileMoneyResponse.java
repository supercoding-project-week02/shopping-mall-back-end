package com.supercoding.shoppingmallbackend.dto.response;

import com.supercoding.shoppingmallbackend.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileMoneyResponse {
    private Long leftMoney;

    public static ProfileMoneyResponse from(Profile findProfile) {
        return new ProfileMoneyResponse(findProfile.getPaymoney());
    }
}

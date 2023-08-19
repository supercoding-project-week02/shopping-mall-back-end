package com.supercoding.shoppingmallbackend.controller;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.dto.request.RechargeRequest;
import com.supercoding.shoppingmallbackend.dto.response.ProfileMoneyResponse;
import com.supercoding.shoppingmallbackend.security.AuthHolder;
import com.supercoding.shoppingmallbackend.dto.request.LoginRequest;
import com.supercoding.shoppingmallbackend.dto.request.SignupRequest;
import com.supercoding.shoppingmallbackend.dto.response.LoginResponse;
import com.supercoding.shoppingmallbackend.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "유저 회원가입, 로그인 API")
@RequestMapping("/api/v1/user")
public class UserController {   //TODO: User -> Profile로 명칭 통일 예정

    private final ProfileService profileService;

    @Operation(summary = "회원 가입", description = "구매자, 판매자를 선택하여 회원 가입을 진행합니다.")
    @PostMapping("/signup")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data",
            schema = @Schema(implementation = MultipartFile.class)))
    public CommonResponse<?> signupConsumer(@Valid @ModelAttribute SignupRequest signupRequest,
                                            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){
        profileService.signup(
                signupRequest.getType(),
                signupRequest.getNickname(),
                signupRequest.getPassword(),
                signupRequest.getEmail(),
                signupRequest.getPhoneNumber(),
                profileImage
        );

        return CommonResponse.success("회원가입에 성공했습니다.", null);
    }

    @Operation(summary = "로그인", description = "회원 이메일과 비밀번호를 입력해 로그인을 진행합니다.")
    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse= profileService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return CommonResponse.success("로그인에 성공했습니다", loginResponse);
    }

    @Operation(summary = "남은 요금 조회", description = "토큰을 이용해 유저의 남은 잔액 확인")
    @GetMapping("/recharge")
    public CommonResponse<?> findProfileLeftMoney() {
        Long profileIdx = AuthHolder.getUserIdx();
        ProfileMoneyResponse profileMoneyResponse = profileService.findProfileLeftMoney(profileIdx);
        return CommonResponse.success("유저의 남은 잔액을 조회했습니다", profileMoneyResponse);
    }

    @Operation(summary = "회원 요금 충전", description = "충전할 요금을 받아 충전합니다 ")
    @PostMapping("/recharge")
    public CommonResponse<?> rechargeProfileMoney(@RequestBody RechargeRequest rechargeRequest) {
        Long profileIdx = AuthHolder.getUserIdx();
        profileService.rechargeProfileMoney(profileIdx, rechargeRequest.getRechargeMoney());
        return CommonResponse.success("충전이 완료됐습니다", null);
    }
}

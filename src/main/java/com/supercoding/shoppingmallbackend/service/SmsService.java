package com.supercoding.shoppingmallbackend.service;

import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.ProfileErrorCode;
import com.supercoding.shoppingmallbackend.common.Error.domain.UtilErrorCode;
import com.supercoding.shoppingmallbackend.common.TimeTrace;
import com.supercoding.shoppingmallbackend.common.util.PhoneUtils;
import com.supercoding.shoppingmallbackend.common.util.RandomUtils;
import com.supercoding.shoppingmallbackend.dto.vo.AuthAndTime;
import com.supercoding.shoppingmallbackend.entity.Profile;
import com.supercoding.shoppingmallbackend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    @Value("${sms-key}")
    private String smsKey;

    @Value("${sms-secret-key}")
    private String smsSecretKey;

    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder encoder;
    private final Map<String, AuthAndTime> authenticationMap = new ConcurrentHashMap<>();

    /**
     * 임시 코드 생성 메소드
     * @param phoneNum 임시 코드를 전송할 번호 ex) 01012341234
     */
    public String sendAuthenticationCode(String phoneNum){

//        Message coolsms = new Message(smsKey, smsSecretKey);

        String numStr = RandomUtils.generateAuthCode();

        HashMap<String, String> params = new HashMap<>();
        params.put("to", PhoneUtils.joinPhoneString(phoneNum));
        params.put("from", "01021106737");
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + numStr + "] 입니다.");

//        try { //TODO: 실제 서비스 시 주석 해제
//            coolsms.send(params); // 메시지 전송
        AuthAndTime authAndTime = new AuthAndTime(numStr, LocalDateTime.now().plusMinutes(10).toString());
        authenticationMap.put(phoneNum, authAndTime);
            log.info("key: {}      value: {}", phoneNum, authAndTime);
//        } catch (CoolsmsException e) {
//            throw new CustomException(UtilErrorCode.SEND_ERROR.getErrorCode());
//        }
        return numStr;
    }


    /**
     * 검증 후 임시 비밀번호 반환
     * @param phone 검증 대상이 될 유저의 핸드폰 번호
     * @param authCode 인증 코드
     * @return randomPassword 임시 비밀번호 반환
     */
    public void authenticationSms(String phone, String authCode) {
        // 인증 요청 phone 존재 확인
        AuthAndTime authAndTime = isContainPhone(phone);
        // 인증 유효 시간 10분
        if(isBefore(authAndTime)) throw new CustomException(ProfileErrorCode.AUTH_TIME_EXPIRED);
        // 인증 코드 일치하는지
        if(!authCode.equals(authAndTime.getAuthCode())) throw new CustomException(ProfileErrorCode.NOT_MATCH_VALUE);
        // 인증 key 삭제
        authenticationMap.remove(phone);
    }

    private static boolean isBefore(AuthAndTime authAndTime) {
        return LocalDateTime.now().isBefore(LocalDateTime.parse(authAndTime.getExpiredTime()));
    }

    private AuthAndTime isContainPhone(String phone) {
        log.info("authentication: {}", authenticationMap);
        if(!authenticationMap.containsKey(phone)){
            throw new CustomException(ProfileErrorCode.NOT_FOUND_PHONE);
        }
        return authenticationMap.get(phone);
    }


    @TimeTrace
    @Scheduled(cron = "0 0 1 * * *") //1시간마다 만료된 key 제거
    public void clearAuthenticationMap() {
        authenticationMap.entrySet().forEach((set) -> {
            AuthAndTime authAndTime = authenticationMap.get(set.getKey());
            if(!isBefore(authAndTime)) authenticationMap.remove(set.getKey());
        });
    }

    private String setRandomPassword(String phoneNum) {
        // 휴대폰 번호로 찾기
        Profile findProfile = findProfileByPhoneNum(phoneNum);
        // 비밀번호 생성
        String randomPassword = RandomUtils.generateRandomPassword();
        // 임시 비밀번호 저장
        String encodePassword = encoder.encode(randomPassword);
        findProfile.setPassword(encodePassword);
        return randomPassword;
    }

    private Profile findProfileByPhoneNum(String phoneNum) {
        return profileRepository.findByPhoneNum(phoneNum).orElseThrow(() -> new CustomException(ProfileErrorCode.NOT_FOUND));
    }

}

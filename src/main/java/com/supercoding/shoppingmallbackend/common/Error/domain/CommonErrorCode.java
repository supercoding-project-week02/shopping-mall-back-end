package com.supercoding.shoppingmallbackend.common.Error.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supercoding.shoppingmallbackend.common.Error.ErrorCode;
import com.supercoding.shoppingmallbackend.common.Error.ErrorCodeInterface;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonErrorCode implements ErrorCodeInterface {
    // Common
    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), "형식에 맞지 않는 path variable입니다."),
    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST.value(), "형식에 맞지 않는 query parameter입니다."),
    FAIL_TO_SAVE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 측의 문제로 데이터의 저장에 실패했습니다. 다시 한 번 시도해주세요."),
    INVALID_TYPE_VALUE(400, " Invalid Type Value");


    private final ErrorCode errorCode;

    CommonErrorCode(Integer status, String message) {
        this.errorCode = new ErrorCode(status, message) {};
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

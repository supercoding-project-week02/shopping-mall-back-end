package com.supercoding.shoppingmallbackend.common.Error.domain;

import com.supercoding.shoppingmallbackend.common.Error.ErrorCode;
import com.supercoding.shoppingmallbackend.common.Error.ErrorCodeInterface;
import org.springframework.http.HttpStatus;

public enum PaymentErrorCode implements ErrorCodeInterface {
    CONFLICT_ORDER_NUMBER(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 주문번호입니다."),
    FAIL_TO_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "주문번호 생성에 실패했습니다. 다시 시도해주세요."),
    OVER_AMOUNT(HttpStatus.BAD_REQUEST, "주문 수량이 실제 재고보다 많습니다."),
    NO_CREATED_PAYMENT(HttpStatus.INTERNAL_SERVER_ERROR, "결제정보가 제대로 저장되지 않았습니다."),
    NOT_ENOUGH_PAYMONEY(HttpStatus.BAD_REQUEST, "페이머니가 부족합니다."),
    INVALID_RECHARGE_VALUE(HttpStatus.BAD_REQUEST, "충전할 요금이 허용되지 않는 값입니다."),
    NO_PRODUCT(HttpStatus.BAD_REQUEST, "결제할 상품이 없습니다.");

    private final int status;
    private final String message;

    PaymentErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    PaymentErrorCode(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    @Override
    public ErrorCode getErrorCode() {
        return new ErrorCode(status, message){};
    }
}

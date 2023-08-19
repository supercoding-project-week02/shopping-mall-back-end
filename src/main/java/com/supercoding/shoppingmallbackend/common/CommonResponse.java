package com.supercoding.shoppingmallbackend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supercoding.shoppingmallbackend.common.Error.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@ApiModel("공통응답 폼")
public class CommonResponse<T> {
    @ApiModelProperty(value = "응답 성공 시 true, 실패 시 false", required = true, example = "true")
    private final boolean result;
    @ApiModelProperty(value = "Http 상태 코드", required = true, example = "200")
    private final Integer status;
    @ApiModelProperty(value = "응답 메세지", required = true, example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @ApiModelProperty(value = "응답 데이터", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    static List<Object> emptyList = Collections.emptyList();

    @Builder
    public CommonResponse(boolean result, Integer status, String message,  T data) {
        this.result = result;
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public static <T>CommonResponse<T> success(String message, T data) {
        return new CommonResponse<T>(true, 200, message, data);
    }
    public static <T>CommonResponse<T> fail(ErrorCode code) {
        return new CommonResponse<>(false, code.getStatus(), code.getMessage(), (T) emptyList);
    }
    public static <T extends Exception>CommonResponse<T> fail(T e) {

        return new CommonResponse<T>(false, 500, e.getMessage(), (T) emptyList);
    }

    public String toStream() {
        if (data == null) {
            return "{" +
                    "\"result\":"+ "\"" + result + "\"," +
                    "\"status\":"+ "\"" + status + "\"," +
                    "\"message\":"+ "\"" + message + "\"," +
                    "}";
        }

        return "{" +
                "\"result\":"+ "\"" + result + "\"," +
                "\"status\":"+ "\"" + status + "\"," +
                "\"message\":"+ "\"" + message + "\"," +
                "\"data\":" + data +
                "}";
    }
}

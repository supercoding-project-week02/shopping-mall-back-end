package com.supercoding.shoppingmallbackend.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ApiModel("pagination 결과를 담는 객체")
public class PaginationResponse<T> {
    @ApiModelProperty(required = true, value = "다음 페이지가 있는지 유무", example = "true")
    private boolean hasNext;
    @ApiModelProperty(required = true, value = "이전 페이지가 있는지 유무", example = "false")
    private boolean hasPrevious;
    @ApiModelProperty(required = true, value = "pagination으로 조회한 데이터들")
    private List<T> contents;
}

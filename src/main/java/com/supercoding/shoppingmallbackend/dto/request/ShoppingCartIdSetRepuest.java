package com.supercoding.shoppingmallbackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ApiModel("장바구니id들을 담은 객체")
public class ShoppingCartIdSetRepuest {
    @ApiModelProperty(required = true, value = "장바구니 id set")
    Set<Long> shoppingCartIdSet;
}

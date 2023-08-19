package com.supercoding.shoppingmallbackend.dto.response;

import com.supercoding.shoppingmallbackend.entity.ShoppingCart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ApiModel("쇼핑카트 아이템")
public class ShoppingCartItemResponse {
    @ApiModelProperty(required = true, value = "장바구니 id", example = "1")
    private Long id;
    @ApiModelProperty(required = true, value = "상품을 담은 구매자")
    private ConsumerResponse consumer;
    @ApiModelProperty( required = true, value = "장바구니에 담긴 상품")
    private ProductSimpleResponse product;
    @ApiModelProperty(required = true, value = "장바구니에 담긴 수량", example = "1")
    private Long quantity;

    public static ShoppingCartItemResponse from(ShoppingCart shoppingCart) {
        return ShoppingCartItemResponse.builder()
                .id(shoppingCart.getId())
                .consumer(ConsumerResponse.from(shoppingCart.getConsumer()))
                .product(ProductSimpleResponse.from(shoppingCart.getProduct()))
                .quantity(shoppingCart.getAmount())
                .build();
    }
}

package com.supercoding.shoppingmallbackend.controller;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.CommonErrorCode;
import com.supercoding.shoppingmallbackend.common.util.ApiUtils;
import com.supercoding.shoppingmallbackend.dto.request.ShoppingCartIdSetRepuest;
import com.supercoding.shoppingmallbackend.dto.request.ShoppingCartItemRequest;
import com.supercoding.shoppingmallbackend.dto.response.PaginationResponse;
import com.supercoding.shoppingmallbackend.dto.response.ShoppingCartItemResponse;
import com.supercoding.shoppingmallbackend.service.ShoppingCartService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/shoppingcart")
@RequiredArgsConstructor
@Api(tags = "장바구니 API")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @ApiOperation(value = "장바구니에 상품 세팅(추가 및 수량 변경)", notes = "장바구니에 상품을 추가하거나, 상품의 수량을 변경합니다.")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "Bearer [JWT Token]", required = true, paramType = "header")
    @PostMapping()
    public CommonResponse<ShoppingCartItemResponse> setProduct(
            @RequestBody
            @ApiParam(required = true, value = "어떤 상품을 얼마나 담았는지 알려줄 객체")
            ShoppingCartItemRequest shoppingCartItemRequest) {
        return shoppingCartService.setProduct(shoppingCartItemRequest);
    };

    @ApiOperation(value = "장바구니 전체 조회", notes = "장바구니를 전체 조회합니다.")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "Bearer [JWT Token]", required = true, paramType = "header")
    @GetMapping()
    public CommonResponse<List<ShoppingCartItemResponse>> getShoppingCart(){
        return shoppingCartService.getShoppingCart();
    }

    @ApiOperation(value = "장바구니 전체 조회 (pagination)", notes = "장바구니를 전체 조회합니다. 근데 pagination을 곁들인...")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "Bearer [JWT Token]", required = true, paramType = "header")
    @GetMapping("/query")
    public CommonResponse<PaginationResponse<ShoppingCartItemResponse>> getShoppingCartWithPagination(@RequestParam String page, @RequestParam String size){
        try {
            return shoppingCartService.getShoppingCartWithPagination(Integer.parseInt(page), Integer.parseInt(size));
        } catch(NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }

    @ApiOperation(value = "장바구니 전체 삭제", notes = "장바구니에 담긴 모든 상품을 제거합니다.")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "Bearer [JWT Token]", required = true, paramType = "header")
    @DeleteMapping()
    public CommonResponse<ShoppingCartItemResponse> deleteShoppingCart() {
        return shoppingCartService.softDeleteShoppingCart();
    }

    @ApiOperation(value = "장바구니 일부 삭제", notes = "장바구니에 담긴 지정된 상품을 제거합니다.")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "Bearer [JWT Token]", required = true, paramType = "header")
    @DeleteMapping("/selected")
    public CommonResponse<Object> deleteShoppingCart(
            @RequestBody @ApiParam(required = true, value = "삭제할 장바구니 id들을 알려줄 객체") ShoppingCartIdSetRepuest shoppingCartIdSetRepuest) {
        return shoppingCartService.softDeleteShoppingCartByIds(shoppingCartIdSetRepuest.getShoppingCartIdSet());
    }
}

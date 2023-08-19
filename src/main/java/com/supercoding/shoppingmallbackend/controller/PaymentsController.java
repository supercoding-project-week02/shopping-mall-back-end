package com.supercoding.shoppingmallbackend.controller;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.CommonErrorCode;
import com.supercoding.shoppingmallbackend.dto.request.PaymentRequest;
import com.supercoding.shoppingmallbackend.dto.response.PaginationResponse;
import com.supercoding.shoppingmallbackend.dto.response.PaymentResponse;
import com.supercoding.shoppingmallbackend.dto.response.PurchaseResponse;
import com.supercoding.shoppingmallbackend.dto.response.SaleResponse;
import com.supercoding.shoppingmallbackend.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Api(tags = "결제, 결제내역 API")
public class PaymentsController {
    private final PaymentService paymentService;

    @PostMapping()
    public CommonResponse<List<PaymentResponse>> processPayment(@RequestBody @ApiParam(name = "결제 요청 객체", value = "배송 받는 사람의 주소, 이름 ,연락처를 알려줄 객체", required = true) PaymentRequest paymentRequest) {
        return paymentService.buyWhole(paymentRequest);
    }

    @ApiOperation(value = "일부 상품 결제하기", notes = "현재 장바구니에 담긴 상품 중 지정된 것을 구매합니다.")
    @PostMapping("/query")
    public CommonResponse<List<PaymentResponse>> processPaymentSelected(
            @RequestParam("id") Set<String> shoppingCartIds,
            @RequestBody @ApiParam(name = "결제 요청 객체", value = "배송 받는 사람의 주소, 이름 ,연락처를 알려줄 객체", required = true) PaymentRequest paymentRequest) {
        try {
            Set<Long> shoppingCartIdSet = shoppingCartIds.stream().map(Long::parseLong).collect(Collectors.toSet());
            return paymentService.buySelected(paymentRequest, shoppingCartIdSet);
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }

    @ApiOperation(value = "구매내역 가져오기", notes = "구매자의 구매내역을 가져옵니다.")
    @GetMapping("/purchased")
    public CommonResponse<List<PurchaseResponse>> getPurchaseHistory() {
        return paymentService.getPurchaseHistory();
    }

    @ApiOperation(value = "구매내역 가져오기 (pagination)", notes = "구매자의 구매내역을 가져옵니다. 그런데 이제 이 pagination을 곁들인...")
    @GetMapping("/purchased/query")
    public CommonResponse<PaginationResponse<PurchaseResponse>> getPurchaseHistory(@RequestParam String page, @RequestParam String size) {
        try {
            return paymentService.getPurchaseHistoryWithPagination(Integer.parseInt(page), Integer.parseInt(size));
        } catch(NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }

    @ApiOperation(value = "판매내역 가져오기", notes = "판매자의 판매내역을 가져옵니다.")
    @GetMapping("/sold")
    public CommonResponse<List<SaleResponse>> getSaleHistory() {
        return paymentService.getSaleHistory();
    }

    @ApiOperation(value = "판매내역 가져오기 (pagination)", notes = "판매자의 판매내역을 가져옵니다. 그런데 이제 이 pagination을 곁들인...")
    @GetMapping("/sold/query")
    public CommonResponse<PaginationResponse<SaleResponse>> getSaleHistory(@RequestParam String page, @RequestParam String size) {
        try {
            return paymentService.getSaleHistoryWithPagination(Integer.parseInt(page), Integer.parseInt((size)));
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }
}

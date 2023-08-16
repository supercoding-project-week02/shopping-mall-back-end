package com.supercoding.shoppingmallbackend.service;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.dto.request.OrderRequest;
import com.supercoding.shoppingmallbackend.entity.Consumer;
import com.supercoding.shoppingmallbackend.repository.ConsumerRepository;
import com.supercoding.shoppingmallbackend.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ConsumerRepository consumerRepository;

    @Transactional(transactionManager = "tmJpa")
    public CommonResponse<Object> setProduct(OrderRequest orderRequest) {
        Long productId = orderRequest.getProductId();
        Long amount = orderRequest.getAmount();

        Consumer consumer = consumerRepository.findById(orderRequest.getConsumerId()).orElseThrow(
                ()->new CustomException(HttpStatus.NOT_FOUND, "구매자 정보를 찾지 못했습니다. consumer_id를 다시 확인해주세요.")
        );
//        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(
//                ()->new CustomException(HttpStatus.NOT_FOUND, "상품 정보를 찾지 못했습니다. product_id를 다시 확인해주세요.")
//        );

        // 장바구니 항목 가져오기
        shoppingCartRepository.findByConsumerIdAndProductId(orderRequest.getConsumerId(), productId).orElseGet(()->null);
        //
    }
}

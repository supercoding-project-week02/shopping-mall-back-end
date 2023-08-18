package com.supercoding.shoppingmallbackend.service;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.*;
import com.supercoding.shoppingmallbackend.common.util.ApiUtils;
import com.supercoding.shoppingmallbackend.common.util.JpaUtils;
import com.supercoding.shoppingmallbackend.dto.ProfileDetail;
import com.supercoding.shoppingmallbackend.dto.request.ShoppingCartItemRequest;
import com.supercoding.shoppingmallbackend.dto.response.PaginationResponse;
import com.supercoding.shoppingmallbackend.dto.response.ProductSimpleResponse;
import com.supercoding.shoppingmallbackend.dto.response.ShoppingCartItemResponse;
import com.supercoding.shoppingmallbackend.entity.Consumer;
import com.supercoding.shoppingmallbackend.entity.Genre;
import com.supercoding.shoppingmallbackend.entity.Product;
import com.supercoding.shoppingmallbackend.entity.ShoppingCart;
import com.supercoding.shoppingmallbackend.repository.*;
import com.supercoding.shoppingmallbackend.security.AuthHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ConsumerRepository consumerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CommonResponse<ShoppingCartItemResponse> setProduct(ShoppingCartItemRequest shoppingCartItemRequest) {
        Long profileId = AuthHolder.getUserIdx();
//        Long profileId = 40L;
        Long productId = shoppingCartItemRequest.getProductId();
        Long addedQuantity = shoppingCartItemRequest.getAmount();

        Consumer consumer = getConsumerByProfileId(profileId);
        Product product = productRepository.findProductById(productId).orElseThrow(
                ()->new CustomException(ProductErrorCode.NOTFOUND_PRODUCT)
        );

        // consuemrId, productId로 장바구니 조회
        ShoppingCart shoppingCartItem = shoppingCartRepository.findByConsumerIdProductId(consumer.getId(), productId).orElse(null);

        if (shoppingCartItem == null) {
            // 장바구니에 존재하지 않다면
            ShoppingCart newData = ShoppingCart.builder()
                    .consumer(consumer)
                    .product(product)
                    .amount(addedQuantity)
                    .build();
            newData.setIsDeleted(false);

            JpaUtils.managedSave(shoppingCartRepository, newData);

            ShoppingCartItemResponse createdData = ShoppingCartItemResponse.from(newData);
            return ApiUtils.success("장바구니에 상품을 성공적으로 추가했습니다.", createdData);
        }

        // 장바구니에 이미 존재한다면
        shoppingCartItem.setAmount(addedQuantity);

        ShoppingCartItemResponse modifiedData = ShoppingCartItemResponse.from(shoppingCartItem);
        return ApiUtils.success("장바구니에 담긴 상품의 수량을 성공적으로 변경하였습니다.", modifiedData);
    }

    public CommonResponse<List<ShoppingCartItemResponse>> getShoppingCart() {
        Long profileId = AuthHolder.getUserIdx();
//        Long profileId = 40L;

        Consumer consumer = getConsumerByProfileId(profileId);

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAllByConsumerId(consumer.getId());

        List<ShoppingCartItemResponse> shoppingCartItemResponses = shoppingCartList.stream()
                .map(ShoppingCartItemResponse::from)
                .collect(Collectors.toList());

        return ApiUtils.success("장바구니를 성공적으로 조회했습니다.", shoppingCartItemResponses);
    }

    @Transactional
    public CommonResponse<ShoppingCartItemResponse> softDeleteShoppingCart() {
        Long profileId = AuthHolder.getUserIdx();
//        Long profileId = 40L;
        Consumer consumer = getConsumerByProfileId(profileId);

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAllByConsumerId(consumer.getId());
        shoppingCartList.forEach(shoppingCart -> {
            shoppingCart.setIsDeleted(true);
        });

        return ApiUtils.success("장바구니의 모든 상품을 성공적으로 삭제했습니다.", null);
    }

    @Transactional
    public CommonResponse<Object> softDeleteShoppingCartByIds(Set<Long> shoppingCartIdSet) {
        Long profileId = AuthHolder.getUserIdx();
//        Long profileId = 40L;
        Consumer consumer = getConsumerByProfileId(profileId);

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAllByConsumerId(consumer.getId());
        shoppingCartList.forEach(shoppingCart -> {
            if (shoppingCartIdSet.contains(shoppingCart.getId())) shoppingCart.setIsDeleted(true);
        });

        return ApiUtils.success("장바구니에서 선택한 상품들을 성공적으로 삭제했습니다.", null);
    }

    public CommonResponse<PaginationResponse<ShoppingCartItemResponse>> getShoppingCartWithPagination(int page, int size) {
//        Long profileId = AuthHolder.getUserIdx();
        Long profileId = 40L;
        Consumer consumer = getConsumerByProfileId(profileId);

        Slice<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByConsumerIdWithPagination(consumer.getId(), PageRequest.of(page, size));
        List<ShoppingCartItemResponse> shoppingCartItemResponses = shoppingCarts.getContent().stream().map(ShoppingCartItemResponse::from).collect(Collectors.toList());

        PaginationResponse<ShoppingCartItemResponse> paginationResponse = new PaginationResponse<>(shoppingCarts.hasNext(), shoppingCarts.hasPrevious(), shoppingCartItemResponses);

        return ApiUtils.success("장바구니를 성공적으로 조회했습니다.", paginationResponse);
    }

    private Consumer getConsumerByProfileId(Long profileId){
        return consumerRepository.findByProfileId(profileId).orElseThrow(()->new CustomException(ConsumerErrorCode.NOT_FOUND_BY_ID));
    }
}

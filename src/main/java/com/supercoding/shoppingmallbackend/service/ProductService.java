package com.supercoding.shoppingmallbackend.service;


import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.*;
import com.supercoding.shoppingmallbackend.common.util.FilePath;
import com.supercoding.shoppingmallbackend.dto.request.ProductListRequest;
import com.supercoding.shoppingmallbackend.dto.request.ProductRequestBase;
import com.supercoding.shoppingmallbackend.dto.response.ProductDetailResponse;
import com.supercoding.shoppingmallbackend.dto.response.ProductImageResponse;
import com.supercoding.shoppingmallbackend.dto.response.ProductListResponse;
import com.supercoding.shoppingmallbackend.entity.*;
import com.supercoding.shoppingmallbackend.repository.*;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AwsS3Service awsS3Service;
    private final GenreRepository genreRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductContentImageRepository productContentImageRepository;

    @Transactional
    public ProductDetailResponse getProductByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.NOTFOUND_PRODUCT.getErrorCode()));
        List<ProductContentImage> productContentImageList = product.getProductContentImages();
        List<ProductImageResponse> productImageResponseList = productContentImageList
                .stream()
                .map(ProductImageResponse::from)
                .collect(Collectors.toList());
        List<Category> categories = categoryRepository.findCategoriesByProductId(product.getId());

        try {
            return ProductDetailResponse.from(product, productImageResponseList, categories);
        } catch (ParseException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
        }
    }

    @Transactional
    public void createProductItem(ProductRequestBase productRequestBase,
                                  MultipartFile thumbFile,
                                  List<MultipartFile> imgFiles,
                                  Long profileIdx) {
        Long validProfileIdx = Optional.ofNullable(profileIdx)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOTFOUND_USER.getErrorCode()));

        Seller seller = sellerRepository.findByProfile_Id(validProfileIdx)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOTFOUND_USER.getErrorCode()));

        Genre genre = genreRepository.findById(productRequestBase.getGenre())
                .orElseThrow(() -> new CustomException(GenreErrorCode.NOT_FOUND));

        Category playerCount = categoryRepository.findByName(productRequestBase.getPlayerCount())
                .orElseThrow(() -> new CustomException(CategoryErrorCode.NOT_FOUND_BY_ID));

        Category playTime = categoryRepository.findByName(productRequestBase.getPlayTime())
                .orElseThrow(() -> new CustomException(CategoryErrorCode.NOT_FOUND_BY_ID));

        Category difficultyLevel = categoryRepository.findByName(productRequestBase.getDifficultyLevel())
                .orElseThrow(() -> new CustomException(CategoryErrorCode.NOT_FOUND_BY_ID));

        boolean imageExists = Optional.ofNullable(imgFiles).isPresent();

        if (imageExists && imgFiles.size() > 5) throw new CustomException(ProductErrorCode.TOO_MANY_FILES);

        try {
            Product product = Product.from(productRequestBase, seller, genre);
            productRepository.save(product);
            if (product.getId() != null) {
                uploadThumbNailImage(thumbFile, product);
                product.addProductCategory(playerCount);
                product.addProductCategory(playTime);
                product.addProductCategory(difficultyLevel);
                if (imageExists) {
                    productContentImageRepository.saveAll(uploadImageFileList(imgFiles, product));
                }

            }
        } catch (ParseException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
        }
    }

    private void uploadThumbNailImage(MultipartFile thumbNailFile, Product product) {
        try {
            if (thumbNailFile != null) {
                String url = awsS3Service.upload(thumbNailFile,
                        FilePath.PRODUCT_THUMB_NAIL_DIR.getPath() + product.getId());
                product.setMainImageUrl(url);
            }
        } catch (IOException e) {
            throw new CustomException(CommonErrorCode.FAIL_TO_SAVE.getErrorCode());
        }
    }

    private List<ProductContentImage> uploadImageFileList(List<MultipartFile> imgList, Product product) {
        return imgList.stream().map(multipartFile -> {
            String uniqueIdentifier = UUID.randomUUID().toString();
            try {
                String url = awsS3Service.upload(multipartFile,
                        FilePath.PRODUCT_CONTENT_DIR.getPath() + product.getId() + uniqueIdentifier);
                return ProductContentImage.from(product, url);
            } catch (IOException e) {
                throw new CustomException(CommonErrorCode.FAIL_TO_SAVE.getErrorCode());
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductByProductId(Long productId, Long profileIdx) {
        Long validProfileIdx = Optional.ofNullable(profileIdx)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOTFOUND_USER.getErrorCode()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.NOTFOUND_PRODUCT));

        if (!Objects.equals(product.getSeller().getProfile().getId(), validProfileIdx)) {
            throw new CustomException(UserErrorCode.NOT_AUTHORIZED.getErrorCode());
        }
        productRepository.deleteById(product.getId());
    }

    @Transactional
    public List<ProductListResponse> getProductList(ProductListRequest productListRequest, Pageable pageable) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Page<ProductListResponse> productList =
                productRepository.findAvailableProductsBySearchCriteria(currentTimestamp, productListRequest, pageable);

        return productList.getContent();
    }

}

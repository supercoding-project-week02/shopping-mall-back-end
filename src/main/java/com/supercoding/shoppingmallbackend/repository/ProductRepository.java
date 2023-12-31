package com.supercoding.shoppingmallbackend.repository;

import com.supercoding.shoppingmallbackend.dto.request.ProductListRequest;
import com.supercoding.shoppingmallbackend.dto.response.ProductListResponse;
import com.supercoding.shoppingmallbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndIsDeletedIsFalse(Long productId);

    @Query("SELECT new com.supercoding.shoppingmallbackend.dto.response.ProductListResponse" +
            "(p.id, p.mainImageUrl, p.title, p.price, p.seller.profile.name, COALESCE(ROUND(AVG(r.rating), 2), 0.0)) " +
            "FROM Product p " +
            "JOIN p.genre g " +
            "JOIN p.productCategories pc " +
            "JOIN pc.category c " +
            "LEFT OUTER JOIN p.reviews r " +
            "WHERE p.amount > 0 " +
            "AND p.closingAt >= :currentTimestamp " +
            "AND (:#{#request.genre} = '전체' OR g.name = :#{#request.genre}) " +
            "AND (:#{#request.searchKeyword} IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:#{#request.searchKeyword}), '%')) " +
            "AND (:#{#request.categorySize} = 0 OR c.name IN (:#{#request.category})) " +
            "GROUP BY p.id "
            )
    Page<ProductListResponse> findAvailableProductsBySearchCriteria(
            @Param("currentTimestamp") Timestamp currentTimestamp,
            @Param("request") ProductListRequest productListRequest,
            Pageable pageable);

}
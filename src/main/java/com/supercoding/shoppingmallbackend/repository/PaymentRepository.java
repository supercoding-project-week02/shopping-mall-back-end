package com.supercoding.shoppingmallbackend.repository;

import com.supercoding.shoppingmallbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderNumber(String orderNumber);

    @Query("select p from Payment p " +
            "join fetch p.consumer c " +
            "join fetch c.profile pf " +
            "join fetch p.product pd " +
            "join fetch pd.seller s " +
            "join fetch pd.genre g " +
            "where p.orderNumber=:orderNumber and p.isDeleted=false " +
            "order by p.paidAt desc")
    List<Payment> findAllByOrderNumber(String orderNumber);

    @Query("select p from Payment p " +
            "join fetch p.consumer c " +
            "join fetch c.profile pf " +
            "join fetch p.product pd " +
            "join fetch pd.seller s " +
            "join fetch pd.genre g " +
            "where c.id=:consumerId and p.isDeleted=false " +
            "order by p.paidAt desc")
    List<Payment> findAllByConsumerId(Long consumerId);

    @Query("select p from Payment p " +
            "join fetch p.consumer c " +
            "join fetch c.profile " +
            "join fetch p.product pd " +
            "join fetch pd.seller s " +
            "join fetch pd.genre g " +
            "where s.id=:sellerId and p.isDeleted=false " +
            "order by p.paidAt desc")
    List<Payment> findAllBySellerId(Long sellerId);
}

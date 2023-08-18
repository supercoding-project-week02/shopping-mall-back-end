package com.supercoding.shoppingmallbackend.entity;

import com.supercoding.shoppingmallbackend.dto.request.PaymentRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "payments")
public class Payment extends CommonField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long paymentId;

    @NotNull
    @Column(name = "order_number", length = 11, nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long paidQuantity;

    @Size(max = 127)
    @NotNull
    @Column(name = "address", nullable = false, length = 127)
    private String receivedAddress;

    @Size(max = 127)
    @NotNull
    @Column(name = "address_detail", nullable = false, length = 127)
    private String receivedAddressDetail;

    @Size(max = 63)
    @NotNull
    @Column(name = "receiver_name", nullable = false, length = 63)
    private String recipientName;

    @Size(max = 15)
    @NotNull
    @Column(name="phone", nullable = false, length = 15)
    private String recipientPhoneNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "product_title", nullable = false, length = 50)
    private String productTitle;

    @Lob
    @Column(name = "product_main_image")
    private String productMainImageUrl;

    @NotNull
    @Column(name = "sold_price_per_one", nullable = false)
    private Long paidPrice;

    @NotNull
    @Column(name = "payment_at", nullable = false)
    private Timestamp paidAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public static Payment from(String orderNumber, ShoppingCart shoppingCart, PaymentRequest request, Timestamp paidAt) {
        Product product = shoppingCart.getProduct();
        return Payment.builder()
                .orderNumber(orderNumber)
                .paidQuantity(shoppingCart.getAmount())
                .receivedAddress(request.getAddress())
                .receivedAddressDetail(request.getAddressDetail())
                .recipientName(request.getReceiverName())
                .recipientPhoneNumber(request.getReceiverPhone())
                .productTitle(product.getTitle())
                .productMainImageUrl(product.getMainImageUrl())
                .paidPrice(product.getPrice())
                .consumer(shoppingCart.getConsumer())
                .product(product)
                .paidAt(paidAt)
                .build();
    }
}
package com.supercoding.shoppingmallbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {

    private Long productId;
    private String mainImageUrl;
    private String title;
    private Long price;
    private String companyName;


}

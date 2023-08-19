package com.supercoding.shoppingmallbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 리스트 조회 DTO")
public class ProductListRequest {
    @ApiModelProperty(value = "검색어", example = "할리갈리")
    private String searchKeyword;
    @ApiModelProperty(value = "장르", required = true, example = "SF")
    private String genre;
    @JsonIgnore
    private List<String> category;

    public int getCategorySize() {
        return category == null || category.isEmpty() ? 0 : category.size();
    }


}
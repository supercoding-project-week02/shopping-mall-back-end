package com.supercoding.shoppingmallbackend.controller;

import com.supercoding.shoppingmallbackend.common.CommonResponse;
import com.supercoding.shoppingmallbackend.common.Error.CustomException;
import com.supercoding.shoppingmallbackend.common.Error.domain.CommonErrorCode;
import com.supercoding.shoppingmallbackend.dto.response.PaginationResponse;
import com.supercoding.shoppingmallbackend.dto.response.ScrapResponse;
import com.supercoding.shoppingmallbackend.security.AuthHolder;
import com.supercoding.shoppingmallbackend.service.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/scrap-list")
@RequiredArgsConstructor
@Api(tags = "찜 목록 API")
public class ScrapController {

    private final ScrapService scrapService;

    @GetMapping()
    @ApiOperation(value = "찜한 상품 전체 조회", notes = "찜 목록 전체를 조회합니다.")
    public CommonResponse<List<ScrapResponse>> getAllScrap() {
        Long profileId = AuthHolder.getProfileIdx();
        return scrapService.getAllScrap(profileId);
    }

    @GetMapping("/query")
    @ApiOperation(value = "찜한 상품 전체 조회 (pagination)", notes = "찜 목록 전체를 조회합니다. 그런데 이제 이 페이지네이션을 곁들인...")
    public CommonResponse<PaginationResponse<ScrapResponse>> getScrapPage(@RequestParam("page") String page, @RequestParam("size") String size) {
        Long profileId = AuthHolder.getProfileIdx();
        try {
            return scrapService.getScrapPage(profileId, Integer.parseInt(page), Integer.parseInt(size));
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }

    @PostMapping("/query")
    @ApiOperation(value = "찜하기", notes = "찜 목록에 추가합니다.")
    public CommonResponse<List<ScrapResponse>> addScrap(@RequestParam("product-id") Set<String> productIds) {
        Long profileId = AuthHolder.getProfileIdx();
        try {
            Set<Long> productIdSet = productIds.stream().map(Long::parseLong).collect(Collectors.toSet());
            return scrapService.addScrap(profileId, productIdSet);
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_QUERY_PARAMETER);
        }
    }

    @DeleteMapping("/query")
    @ApiOperation(value = "찜 취소", notes = "찜 목록에서 제거합니다.")
    public CommonResponse<List<ScrapResponse>> deleteScrap(@RequestParam("id") Set<String> scrapIds) {
        Long profileId = AuthHolder.getProfileIdx();
        try {
            Set<Long> scrapIdSet = scrapIds.stream().map(Long::parseLong).collect(Collectors.toSet());
            return scrapService.softDeleteScrap(profileId, scrapIdSet);
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.INVALID_PATH_VARIABLE);
        }
    }
}

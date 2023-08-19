package com.supercoding.shoppingmallbackend.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ApiModel("요청 리퀘스트의 리스트를 담은 객체")
public class ListRequest<T> {
    private List<T> contents;
}

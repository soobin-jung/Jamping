package com.jam.ping.global.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record KeywordPageRequest(
        @Size(max = 100, message = "검색어는 100자 이하여야 합니다.")
        String keyword,
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        Integer page,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
        Integer size
) {
    public KeywordPageRequest {
        if (keyword == null) keyword = "";
        if (page == null) page = 0;
        if (size == null) size = 10;
    }
}

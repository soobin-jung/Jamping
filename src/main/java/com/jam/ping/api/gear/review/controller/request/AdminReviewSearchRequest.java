package com.jam.ping.api.gear.review.controller.request;

import com.jam.ping.api.gear.review.code.ReviewStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record AdminReviewSearchRequest(
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        Integer page,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
        Integer size,
        String sort,
        @Pattern(regexp = "asc|desc", message = "정렬 방향은 asc 또는 desc 이어야 합니다.")
        String direction,
        ReviewStatus status
) {
    public AdminReviewSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt";
        if (direction == null) direction = "desc";
    }
}

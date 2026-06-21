package com.jam.ping.campsite.controller.response;

import com.jam.ping.campsite.domain.CampSite;
import com.jam.ping.regeion.district.code.DistrictCode;
import com.jam.ping.regeion.region.code.RegionCode;
import java.util.List;
import org.springframework.data.domain.Page;

public record CampSitePageResponse(
        List<CampSiteResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        String keyword,
        RegionCode regionCode,
        DistrictCode districtCode
) {

    public static CampSitePageResponse from(
            Page<CampSite> campSitePage,
            String keyword,
            RegionCode regionCode,
            DistrictCode districtCode
    ) {
        return new CampSitePageResponse(
                campSitePage.getContent().stream()
                        .map(CampSiteResponse::from)
                        .toList(),
                campSitePage.getNumber(),
                campSitePage.getSize(),
                campSitePage.getTotalElements(),
                campSitePage.getTotalPages(),
                campSitePage.hasNext(),
                campSitePage.hasPrevious(),
                keyword,
                regionCode,
                districtCode
        );
    }
}

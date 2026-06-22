package com.jam.ping.api.campsite.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.regeion.district.code.DistrictCode;
import com.jam.ping.api.regeion.region.code.RegionCode;
import java.time.LocalTime;

public record CampSiteResponse(
        Long id,
        String name,
        String link,
        RegionCode regionCode,
        DistrictCode districtCode,
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkInTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOutTime
) {

    public static CampSiteResponse from(CampSite campSite) {
        return new CampSiteResponse(
                campSite.getId(),
                campSite.getName(),
                campSite.getLink(),
                campSite.getRegionCode(),
                campSite.getDistrictCode(),
                campSite.getCheckInTime(),
                campSite.getCheckOutTime()
        );
    }
}

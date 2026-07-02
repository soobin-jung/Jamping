package com.jam.ping.api.campsite.dto;

import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.regeion.district.code.DistrictCode;
import com.jam.ping.api.regeion.region.code.RegionCode;
import java.time.LocalTime;

public record CampSiteDto(
        Long id,
        String name,
        String link,
        RegionCode regionCode,
        DistrictCode districtCode,
        LocalTime checkInTime,
        LocalTime checkOutTime
) {

    public static CampSiteDto from(CampSite campSite) {
        return new CampSiteDto(
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

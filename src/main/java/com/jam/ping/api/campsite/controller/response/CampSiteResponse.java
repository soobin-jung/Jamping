package com.jam.ping.api.campsite.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jam.ping.api.campsite.dto.CampSiteDto;
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

    public static CampSiteResponse from(CampSiteDto dto) {
        return new CampSiteResponse(
                dto.id(), dto.name(), dto.link(),
                dto.regionCode(), dto.districtCode(),
                dto.checkInTime(), dto.checkOutTime()
        );
    }
}

package com.jam.ping.campsite.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jam.ping.regeion.district.code.DistrictCode;
import com.jam.ping.regeion.region.code.RegionCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public record CampSiteRequest(
        @NotBlank(message = "캠핑장 이름은 필수입니다.")
        @Size(max = 100, message = "캠핑장 이름은 100자 이하여야 합니다.")
        String name,
        @Size(max = 1000, message = "링크는 1000자 이하여야 합니다.")
        @Pattern(
                regexp = "^$|https?://.+",
                message = "링크는 http:// 또는 https:// 형식이어야 합니다."
        )
        String link,
        @NotNull(message = "지역구는 필수입니다.")
        RegionCode regionCode,
        @NotNull(message = "자치구는 필수입니다.")
        DistrictCode districtCode,
        @NotNull(message = "입실 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkInTime,
        @NotNull(message = "퇴실 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOutTime
) {
}

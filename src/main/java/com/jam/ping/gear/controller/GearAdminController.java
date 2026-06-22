package com.jam.ping.gear.controller;

import com.jam.ping.gear.controller.request.GearRequest;
import com.jam.ping.gear.controller.response.GearPageResponse;
import com.jam.ping.gear.controller.response.GearResponse;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.gear.service.GearService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/gears")
public class GearAdminController {

    private final GearService gearService;

    @GetMapping
    public ApiRes<GearPageResponse> getGears(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long makerId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<GearPageResponse>()
                .successData(GearPageResponse.from(
                        gearService.getGears(categoryId, makerId, keyword, page, size),
                        categoryId,
                        makerId,
                        keyword == null ? "" : keyword.trim()
                ));
    }

    @GetMapping("/{gearId}")
    public ApiRes<GearResponse> getGear(@PathVariable Long gearId) {
        return new ApiRes<GearResponse>()
                .successData(GearResponse.from(gearService.getGear(gearId)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<GearResponse>> createGear(
            @Valid @RequestBody GearRequest request
    ) {
        Gear gear = gearService.createGear(
                request.name(),
                request.link(),
                request.imageUrl(),
                request.categoryId(),
                request.makerId(),
                request.memo()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<GearResponse>()
                        .successData(GearResponse.from(gear))
                        .manipulationOne(gear.getId())
                        .responseMsg("용품이 등록되었습니다."));
    }

    @PutMapping("/{gearId}")
    public ApiRes<GearResponse> updateGear(
            @PathVariable Long gearId,
            @Valid @RequestBody GearRequest request
    ) {
        Gear gear = gearService.updateGear(
                gearId,
                request.name(),
                request.link(),
                request.imageUrl(),
                request.categoryId(),
                request.makerId(),
                request.memo()
        );

        return new ApiRes<GearResponse>()
                .successData(GearResponse.from(gear))
                .manipulationOne(gear.getId())
                .responseMsg("용품이 수정되었습니다.");
    }

    @DeleteMapping("/{gearId}")
    public ApiRes<Void> deleteGear(@PathVariable Long gearId) {
        gearService.deleteGear(gearId);

        return new ApiRes<Void>()
                .manipulationOne(gearId)
                .responseMsg("용품이 삭제되었습니다.");
    }

}

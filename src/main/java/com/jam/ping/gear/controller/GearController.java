package com.jam.ping.gear.controller;

import com.jam.ping.gear.controller.request.GearRequest;
import com.jam.ping.gear.controller.response.GearPageResponse;
import com.jam.ping.gear.controller.response.GearResponse;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.gear.service.GearService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.user.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class GearController {

    private final GearService gearService;

    /**
     * 관리자 화면에서 사용하는 용품 목록을 카테고리, 메이커, 검색어, 페이지 조건으로 조회합니다.
     */
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

    /**
     * 단건 용품 정보를 조회합니다.
     */
    @GetMapping("/{gearId}")
    public ApiRes<GearResponse> getGear(@PathVariable Long gearId) {
        return new ApiRes<GearResponse>()
                .successData(GearResponse.from(gearService.getGear(gearId)));
    }

    /**
     * 새로운 용품을 생성합니다.
     */
    @PostMapping
    public ResponseEntity<ApiRes<GearResponse>> createGear(
            @Valid @RequestBody GearRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Gear gear = gearService.createGear(
                request.name(),
                request.link(),
                request.imageUrl(),
                request.categoryId(),
                request.makerId(),
                request.memo(),
                resolveActorUserId(oauth2User)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<GearResponse>()
                        .successData(GearResponse.from(gear))
                        .manipulationOne(gear.getId())
                        .responseMsg("용품이 등록되었습니다."));
    }

    /**
     * 기존 용품 정보를 수정합니다.
     */
    @PutMapping("/{gearId}")
    public ApiRes<GearResponse> updateGear(
            @PathVariable Long gearId,
            @Valid @RequestBody GearRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Gear gear = gearService.updateGear(
                gearId,
                request.name(),
                request.link(),
                request.imageUrl(),
                request.categoryId(),
                request.makerId(),
                request.memo(),
                resolveActorUserId(oauth2User)
        );

        return new ApiRes<GearResponse>()
                .successData(GearResponse.from(gear))
                .manipulationOne(gear.getId())
                .responseMsg("용품이 수정되었습니다.");
    }

    /**
     * 선택한 용품을 삭제합니다.
     */
    @DeleteMapping("/{gearId}")
    public ApiRes<Void> deleteGear(@PathVariable Long gearId) {
        gearService.deleteGear(gearId);

        return new ApiRes<Void>()
                .manipulationOne(gearId)
                .responseMsg("용품이 삭제되었습니다.");
    }

    /**
     * 생성자와 수정자 기록에 사용할 사용자 ID를 반환합니다.
     */
    private Long resolveActorUserId(CustomOAuth2User oauth2User) {
        if (oauth2User == null) {
            return null;
        }

        return oauth2User.getUserId();
    }
}

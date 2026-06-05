package com.jam.ping.maker.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.maker.controller.request.MakerRequest;
import com.jam.ping.maker.controller.response.MakerPageResponse;
import com.jam.ping.maker.controller.response.MakerResponse;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.service.MakerService;
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
@RequestMapping("/admin/makers")
public class MakerController {

    private final MakerService makerService;

    /**
     * 관리자 화면에서 사용하는 메이커 목록을 검색 조건과 페이지 정보로 조회합니다.
     */
    @GetMapping
    public ApiRes<MakerPageResponse> getMakers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<MakerPageResponse>()
                .successData(MakerPageResponse.from(
                        makerService.getMakers(keyword, page, size),
                        keyword == null ? "" : keyword.trim()
                ));
    }

    /**
     * 단건 메이커 정보를 조회합니다.
     */
    @GetMapping("/{makerId}")
    public ApiRes<MakerResponse> getMaker(@PathVariable Long makerId) {
        return new ApiRes<MakerResponse>()
                .successData(MakerResponse.from(makerService.getMaker(makerId)));
    }

    /**
     * 새로운 메이커를 생성합니다.
     */
    @PostMapping
    public ResponseEntity<ApiRes<MakerResponse>> createMaker(
            @Valid @RequestBody MakerRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Maker maker = makerService.createMaker(
                request.name(),
                request.nameEng(),
                request.homepageUrl(),
                resolveActorUserId(oauth2User)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<MakerResponse>()
                        .successData(MakerResponse.from(maker))
                        .manipulationOne(maker.getId())
                        .responseMsg("메이커가 등록되었습니다."));
    }

    /**
     * 기존 메이커 정보를 수정합니다.
     */
    @PutMapping("/{makerId}")
    public ApiRes<MakerResponse> updateMaker(
            @PathVariable Long makerId,
            @Valid @RequestBody MakerRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Maker maker = makerService.updateMaker(
                makerId,
                request.name(),
                request.nameEng(),
                request.homepageUrl(),
                resolveActorUserId(oauth2User)
        );

        return new ApiRes<MakerResponse>()
                .successData(MakerResponse.from(maker))
                .manipulationOne(maker.getId())
                .responseMsg("메이커가 수정되었습니다.");
    }

    /**
     * 선택한 메이커를 삭제합니다.
     */
    @DeleteMapping("/{makerId}")
    public ApiRes<Void> deleteMaker(@PathVariable Long makerId) {
        makerService.deleteMaker(makerId);

        return new ApiRes<Void>()
                .manipulationOne(makerId)
                .responseMsg("메이커가 삭제되었습니다.");
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

package com.jam.ping.maker.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.global.security.AuthUtils;
import com.jam.ping.maker.controller.request.MakerRequest;
import com.jam.ping.maker.controller.response.MakerPageResponse;
import com.jam.ping.maker.controller.response.MakerResponse;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.service.MakerService;
import com.jam.ping.user.main.oauth.CustomOAuth2User;
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
public class MakerAdminController {

    private final MakerService makerService;

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

    @GetMapping("/{makerId}")
    public ApiRes<MakerResponse> getMaker(@PathVariable Long makerId) {
        return new ApiRes<MakerResponse>()
                .successData(MakerResponse.from(makerService.getMaker(makerId)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<MakerResponse>> createMaker(
            @Valid @RequestBody MakerRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Maker maker = makerService.createMaker(
                request.name(),
                request.nameEng(),
                request.homepageUrl(),
                AuthUtils.getActorUserId(oauth2User)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<MakerResponse>()
                        .successData(MakerResponse.from(maker))
                        .manipulationOne(maker.getId())
                        .responseMsg("메이커가 등록되었습니다."));
    }

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
                AuthUtils.getActorUserId(oauth2User)
        );

        return new ApiRes<MakerResponse>()
                .successData(MakerResponse.from(maker))
                .manipulationOne(maker.getId())
                .responseMsg("메이커가 수정되었습니다.");
    }

    @DeleteMapping("/{makerId}")
    public ApiRes<Void> deleteMaker(@PathVariable Long makerId) {
        makerService.deleteMaker(makerId);

        return new ApiRes<Void>()
                .manipulationOne(makerId)
                .responseMsg("메이커가 삭제되었습니다.");
    }

}

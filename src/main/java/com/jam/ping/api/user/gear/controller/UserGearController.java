package com.jam.ping.api.user.gear.controller;

import com.jam.ping.global.request.PageRequest;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.api.user.gear.controller.request.UserGearRequest;
import com.jam.ping.api.user.gear.controller.response.UserGearPageResponse;
import com.jam.ping.api.user.gear.controller.response.UserGearResponse;
import com.jam.ping.api.user.gear.dto.UserGearDto;
import com.jam.ping.api.user.gear.service.UserGearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-gears")
public class UserGearController {

    private final UserGearService userGearService;

    @GetMapping
    public ApiRes<UserGearPageResponse> getUserGears(@Valid @ModelAttribute PageRequest request) {
        return new ApiRes<UserGearPageResponse>()
                .successData(UserGearPageResponse.from(userGearService.getUserGears(request.page(), request.size())));
    }

    @PostMapping
    public ResponseEntity<ApiRes<UserGearResponse>> createUserGear(@Valid @RequestBody UserGearRequest request) {
        UserGearDto userGear = userGearService.createUserGear(
                request.categoryId(),
                request.makerId(),
                request.gearId(),
                request.name(),
                request.memo()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<UserGearResponse>()
                        .successData(UserGearResponse.from(userGear))
                        .manipulationOne(userGear.id())
                        .responseMsg("장비가 등록되었습니다."));
    }

    @PutMapping("/{userGearId}")
    public ApiRes<UserGearResponse> updateUserGear(
            @PathVariable Long userGearId,
            @Valid @RequestBody UserGearRequest request
    ) {
        UserGearDto userGear = userGearService.updateUserGear(
                userGearId,
                request.categoryId(),
                request.makerId(),
                request.gearId(),
                request.name(),
                request.memo()
        );

        return new ApiRes<UserGearResponse>()
                .successData(UserGearResponse.from(userGear))
                .manipulationOne(userGear.id())
                .responseMsg("장비가 수정되었습니다.");
    }

    @DeleteMapping("/{userGearId}")
    public ApiRes<Void> deleteUserGear(@PathVariable Long userGearId) {
        userGearService.deleteUserGear(userGearId);

        return new ApiRes<Void>()
                .manipulationOne(userGearId)
                .responseMsg("장비가 삭제되었습니다.");
    }
}

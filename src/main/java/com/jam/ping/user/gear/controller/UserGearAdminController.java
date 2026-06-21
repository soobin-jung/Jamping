package com.jam.ping.user.gear.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.user.gear.controller.response.UserGearPageResponse;
import com.jam.ping.user.gear.controller.response.UserGearResponse;
import com.jam.ping.user.gear.service.UserGearService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/user-gears")
public class UserGearAdminController {

    private final UserGearService userGearService;

    @GetMapping
    public ApiRes<UserGearPageResponse> getUserGears(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<UserGearPageResponse>()
                .successData(UserGearPageResponse.from(
                        userGearService.getAllUserGearsForAdmin(page, size)
                ));
    }

    @GetMapping("/{userGearId}")
    public ApiRes<UserGearResponse> getUserGear(@PathVariable Long userGearId) {
        return new ApiRes<UserGearResponse>()
                .successData(UserGearResponse.from(userGearService.getUserGear(userGearId)));
    }

    @DeleteMapping("/{userGearId}")
    public ApiRes<Void> deleteUserGear(@PathVariable Long userGearId) {
        userGearService.deleteUserGearByAdmin(userGearId);

        return new ApiRes<Void>()
                .manipulationOne(userGearId)
                .responseMsg("장비 기록이 삭제되었습니다.");
    }
}

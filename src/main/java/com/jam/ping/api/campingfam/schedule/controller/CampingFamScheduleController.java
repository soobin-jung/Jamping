package com.jam.ping.api.campingfam.schedule.controller;

import com.jam.ping.api.campingfam.schedule.controller.request.CampingFamScheduleTempRequest;
import com.jam.ping.api.campingfam.schedule.controller.response.CampingFamScheduleTempResponse;
import com.jam.ping.api.campingfam.schedule.domain.CampingFamScheduleTemp;
import com.jam.ping.api.campingfam.schedule.service.CampingFamScheduleTempService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AuthUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camping-fams/{campingFamId}/schedule/temps")
public class CampingFamScheduleController {

    private final CampingFamScheduleTempService campingFamScheduleTempService;

    @PostMapping
    public ResponseEntity<ApiRes<List<CampingFamScheduleTempResponse>>> submitDates(
            @PathVariable Long campingFamId,
            @Valid @RequestBody CampingFamScheduleTempRequest request
    ) {
        Long userId = AuthUtils.getCurrentUserId();
        List<CampingFamScheduleTemp> temps = campingFamScheduleTempService.submitDates(
                campingFamId, userId, request.dates()
        );
        List<CampingFamScheduleTempResponse> responses = temps.stream()
                .map(CampingFamScheduleTempResponse::from)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<List<CampingFamScheduleTempResponse>>()
                        .successData(responses)
                        .responseMsg("가능한 날짜가 제출되었습니다."));
    }

    @GetMapping
    public ApiRes<List<CampingFamScheduleTempResponse>> getTempDates(@PathVariable Long campingFamId) {
        List<CampingFamScheduleTempResponse> responses = campingFamScheduleTempService.getTempDates(campingFamId)
                .stream()
                .map(CampingFamScheduleTempResponse::from)
                .toList();
        return new ApiRes<List<CampingFamScheduleTempResponse>>().successData(responses);
    }

    @DeleteMapping
    public ApiRes<Void> deleteMyDates(@PathVariable Long campingFamId) {
        Long userId = AuthUtils.getCurrentUserId();
        campingFamScheduleTempService.deleteMyDates(campingFamId, userId);
        return new ApiRes<Void>().responseMsg("가능한 날짜가 삭제되었습니다.");
    }
}

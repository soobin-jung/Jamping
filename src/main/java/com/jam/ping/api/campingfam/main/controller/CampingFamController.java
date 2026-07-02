package com.jam.ping.api.campingfam.main.controller;

import com.jam.ping.api.campingfam.main.controller.request.CampingFamRequest;
import com.jam.ping.api.campingfam.main.controller.response.CampingFamResponse;
import com.jam.ping.api.campingfam.main.dto.CampingFamDto;
import com.jam.ping.api.campingfam.main.service.CampingFamService;
import com.jam.ping.api.campingfam.schedule.controller.request.CampingFamFinalizeScheduleRequest;
import com.jam.ping.global.response.ApiRes;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camping-fams")
public class CampingFamController {

    private final CampingFamService campingFamService;

    @GetMapping("/{id}")
    public ApiRes<CampingFamResponse> getCampingFam(@PathVariable Long id) {
        return new ApiRes<CampingFamResponse>()
                .successData(CampingFamResponse.from(campingFamService.getCampingFam(id)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<CampingFamResponse>> createCampingFam(
            @Valid @RequestBody CampingFamRequest request
    ) {
        CampingFamDto campingFamDto = campingFamService.createCampingFam(
                request.name(),
                request.campSiteId(),
                request.reservationSites()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<CampingFamResponse>()
                        .successData(CampingFamResponse.from(campingFamDto))
                        .manipulationOne(campingFamDto.id())
                        .responseMsg("캠핑팸이 생성되었습니다."));
    }

    @PutMapping("/{id}/schedule")
    public ApiRes<CampingFamResponse> finalizeSchedule(
            @PathVariable Long id,
            @Valid @RequestBody CampingFamFinalizeScheduleRequest request
    ) {
        CampingFamDto campingFamDto = campingFamService.finalizeSchedule(
                id,
                request.campingStartDate(),
                request.campingEndDate()
        );
        return new ApiRes<CampingFamResponse>()
                .successData(CampingFamResponse.from(campingFamDto))
                .manipulationOne(campingFamDto.id())
                .responseMsg("일정이 확정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ApiRes<Void> deleteCampingFam(@PathVariable Long id) {
        campingFamService.deleteCampingFam(id);
        return new ApiRes<Void>()
                .manipulationOne(id)
                .responseMsg("캠핑팸이 삭제되었습니다.");
    }
}

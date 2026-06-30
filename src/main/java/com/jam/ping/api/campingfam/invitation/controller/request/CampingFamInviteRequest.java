package com.jam.ping.api.campingfam.invitation.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CampingFamInviteRequest(
        @NotEmpty(message = "초대할 이메일을 입력해주세요.")
        List<@Email(message = "올바른 이메일 형식이 아닙니다.") String> emails
) {}

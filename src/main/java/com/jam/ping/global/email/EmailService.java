package com.jam.ping.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public void sendCampingFamInvitation(String to, String campingFamName, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject("[Jamping] " + campingFamName + " 캠핑팸에 초대되었습니다");
            helper.setText(buildHtml(campingFamName, token), true);
            mailSender.send(message);
            log.info("[초대 이메일 발송] to={}, campingFam={}", to, campingFamName);
        } catch (MessagingException e) {
            log.error("[초대 이메일 발송 실패] to={}, error={}", to, e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    private String buildHtml(String campingFamName, String token) {
        String link = frontendUrl + "/join?token=" + token;
        return """
                <div style="font-family:-apple-system,sans-serif;max-width:520px;margin:0 auto;padding:40px 24px;color:#333;">
                  <div style="font-size:28px;margin-bottom:8px;">🏕️</div>
                  <h2 style="margin:0 0 16px;color:#1a1a2e;">캠핑팸 초대</h2>
                  <p style="margin:0 0 8px;font-size:15px;">
                    <strong>%s</strong> 캠핑팸에 초대되었습니다!
                  </p>
                  <p style="margin:0 0 28px;font-size:14px;color:#666;">
                    아래 버튼을 눌러 소셜 로그인 후 캠핑팸에 참여하세요.
                  </p>
                  <a href="%s"
                     style="display:inline-block;padding:13px 28px;background:#4a90d9;color:white;
                            border-radius:8px;text-decoration:none;font-size:15px;font-weight:600;">
                    초대 수락하기
                  </a>
                  <p style="margin-top:32px;font-size:12px;color:#aaa;">
                    이 링크는 7일 후 만료됩니다. 본인이 요청하지 않은 경우 무시하세요.
                  </p>
                </div>
                """.formatted(campingFamName, link);
    }
}

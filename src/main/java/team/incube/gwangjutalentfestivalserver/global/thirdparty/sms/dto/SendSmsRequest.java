package team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsMessage;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsRequest {
    private SmsType type;
    private String from;
    private String content;
    private List<SmsMessage> messages;
}
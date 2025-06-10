package team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage {
    String to;
    String subject;
    String content;
}

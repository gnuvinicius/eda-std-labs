package br.dev.garage474.application.dto;

import br.dev.garage474.domain.model.Subscription;
import lombok.Getter;

@Getter
public class SubscriptionResponse {

    private Long id;
    private String planName;
    private String status;

    public static SubscriptionResponse fromEntity(Subscription sub) {
        SubscriptionResponse dto = new SubscriptionResponse();
        dto.id = sub.id;
        dto.planName = sub.getPlanName();
        dto.status = sub.getStatus().name();
        return dto;
    }
}

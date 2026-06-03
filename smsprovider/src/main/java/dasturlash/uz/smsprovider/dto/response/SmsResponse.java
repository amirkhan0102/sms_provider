package dasturlash.uz.smsprovider.dto.response;

import dasturlash.uz.smsprovider.enums.SmsStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SmsResponse {
    private Long id;
    private Long clientId;
    private String phone;
    private String text;
    private BigDecimal price;
    private SmsStatus status;
    private LocalDateTime createdDate;
}
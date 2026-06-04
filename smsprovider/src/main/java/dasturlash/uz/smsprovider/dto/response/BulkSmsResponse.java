package dasturlash.uz.smsprovider.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BulkSmsResponse {
    private Integer totalCount;
    private BigDecimal totalPrice;
    private BigDecimal balanceAfter;
    private List<SmsResponse> smsList;
}
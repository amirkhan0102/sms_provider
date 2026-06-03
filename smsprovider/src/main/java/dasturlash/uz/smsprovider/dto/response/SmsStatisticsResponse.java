package dasturlash.uz.smsprovider.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SmsStatisticsResponse {
    private Long todaySmsCount;
    private Long totalSmsCount;
    private BigDecimal todayIncome;
    private BigDecimal totalIncome;
}
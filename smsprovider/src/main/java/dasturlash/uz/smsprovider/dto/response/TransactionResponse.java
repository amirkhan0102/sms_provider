package dasturlash.uz.smsprovider.dto.response;

import dasturlash.uz.smsprovider.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long clientId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime createdDate;
}
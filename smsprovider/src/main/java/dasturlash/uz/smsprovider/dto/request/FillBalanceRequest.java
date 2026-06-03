package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FillBalanceRequest {

    @NotNull(message = "Amount bo'sh bo'lmasligi kerak")
    @DecimalMin(value = "1.0", message = "Amount 1 so'mdan kam bo'lmasligi kerak")
    private BigDecimal amount;
}
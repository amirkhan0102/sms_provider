package dasturlash.uz.smsprovider.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopClientResponse {
    private Long clientId;
    private String companyName;
    private Long smsCount;
}
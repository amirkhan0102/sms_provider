package dasturlash.uz.smsprovider.dto.response;

import dasturlash.uz.smsprovider.enums.GeneralStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ClientResponse {
    private Long id;
    private String companyName;
    private String ownerName;
    private String ownerSurname;
    private String phone;
    private String email;
    private String login;
    private BigDecimal balance;
    private GeneralStatus status;
    private LocalDateTime createdDate;
}
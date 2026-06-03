package dasturlash.uz.smsprovider.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientLoginResponse {
    private Long id;
    private String companyName;
    private String login;
    private String token;
}
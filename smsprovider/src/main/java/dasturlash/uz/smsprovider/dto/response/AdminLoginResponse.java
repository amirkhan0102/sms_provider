package dasturlash.uz.smsprovider.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginResponse {

    private Long id;
    private String name;
    private String surname;
    private String login;
    private String role;
    private String token;

}
package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientLoginRequest {

    @NotBlank(message = "Login bo'sh bo'lmasligi kerak")
    private String login;

    @NotBlank(message = "Password bo'sh bo'lmasligi kerak")
    private String password;
}
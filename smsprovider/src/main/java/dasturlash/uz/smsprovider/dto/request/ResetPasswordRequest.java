package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Login bo'sh bo'lmasligi kerak")
    private String login;

    @NotBlank(message = "Yangi password bo'sh bo'lmasligi kerak")
    @Size(min = 6, message = "Password kamida 6 belgidan iborat bo'lishi kerak")
    private String newPassword;
}
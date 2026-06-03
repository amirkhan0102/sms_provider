package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientRegistrationRequest {

    @NotBlank(message = "Company name bo'sh bo'lmasligi kerak")
    private String companyName;

    @NotBlank(message = "Owner name bo'sh bo'lmasligi kerak")
    private String ownerName;

    @NotBlank(message = "Owner surname bo'sh bo'lmasligi kerak")
    private String ownerSurname;

    @NotBlank(message = "Phone bo'sh bo'lmasligi kerak")
    @Pattern(regexp = "^\\+?[0-9]{9,13}$", message = "Phone format noto'g'ri")
    private String phone;

    @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
    @Email(message = "Email format noto'g'ri")
    private String email;

    @NotBlank(message = "Login bo'sh bo'lmasligi kerak")
    @Size(min = 4, max = 50, message = "Login 4-50 belgidan iborat bo'lishi kerak")
    private String login;

    @NotBlank(message = "Password bo'sh bo'lmasligi kerak")
    @Size(min = 6, message = "Password kamida 6 belgidan iborat bo'lishi kerak")
    private String password;
}
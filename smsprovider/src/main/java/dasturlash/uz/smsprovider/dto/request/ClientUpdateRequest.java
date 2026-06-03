package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientUpdateRequest {

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
}
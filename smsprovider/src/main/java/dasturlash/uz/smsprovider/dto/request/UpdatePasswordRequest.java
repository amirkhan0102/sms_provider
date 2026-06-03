package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UpdatePasswordRequest {

    @NotBlank(message = "Eski password bo'sh bo'lmasligi kerak")
    private String oldPassword;

    @NotBlank(message = "Yangi password bo'sh bo'lmasligi kerak")
    @Size(min = 6, message = "New password will be at least 6 items")
    private String newPassword;

}

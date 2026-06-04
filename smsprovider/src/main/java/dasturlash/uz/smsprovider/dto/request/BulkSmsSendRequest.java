package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BulkSmsSendRequest {

    @NotEmpty(message = "Telefon raqamlar ro'yxati bo'sh bo'lmasligi kerak")
    @Size(max = 1000, message = "Maksimum 1000 ta telefon raqam yuborish mumkin")
    private List<String> phones;

    @NotBlank(message = "SMS text bo'sh bo'lmasligi kerak")
    private String text;
}

package dasturlash.uz.smsprovider.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledSmsRequest {

    @NotBlank(message = "Phone bo'sh bo'lmasligi kerak")
    @Pattern(regexp = "^\\+?[0-9]{9,13}$", message = "Phone format noto'g'ri")
    private String phone;

    @NotBlank(message = "SMS text bo'sh bo'lmasligi kerak")
    private String text;

    @NotNull(message = "Scheduled date bo'sh bo'lmasligi kerak")
    @Future(message = "Scheduled date kelajakdagi vaqt bo'lishi kerak")
    private LocalDateTime scheduledDate;
}
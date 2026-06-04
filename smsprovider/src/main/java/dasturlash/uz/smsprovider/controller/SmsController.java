package dasturlash.uz.smsprovider.controller;

import dasturlash.uz.smsprovider.dto.request.BulkSmsSendRequest;
import dasturlash.uz.smsprovider.dto.request.SmsSendRequest;
import dasturlash.uz.smsprovider.dto.response.BulkSmsResponse;
import dasturlash.uz.smsprovider.dto.response.SmsResponse;
import dasturlash.uz.smsprovider.security.CustomUserDetails;
import dasturlash.uz.smsprovider.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SmsSendRequest request) {
        request.setClientId(userDetails.getId());
        return ResponseEntity.ok(smsService.sendSms(request));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<SmsResponse>> getMySms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                smsService.getMySms(userDetails.getId(), fromDate, toDate, pageable));
    }
    @PostMapping("/bulk")
    public ResponseEntity<BulkSmsResponse> sendBulkSms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BulkSmsSendRequest request) {
        return ResponseEntity.ok(
                smsService.sendBulkSms(request, userDetails.getId()));
    }
}
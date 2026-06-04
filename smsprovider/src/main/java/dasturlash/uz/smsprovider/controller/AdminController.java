package dasturlash.uz.smsprovider.controller;

import dasturlash.uz.smsprovider.dto.response.*;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.enums.SmsStatus;
import dasturlash.uz.smsprovider.enums.TransactionType;
import dasturlash.uz.smsprovider.service.AdminService;
import dasturlash.uz.smsprovider.service.SmsService;
import dasturlash.uz.smsprovider.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final SmsService smsService;
    private final TransactionService transactionService;


    @GetMapping("/client")
    public ResponseEntity<Page<ClientResponse>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) GeneralStatus status,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdDate").descending());
        return ResponseEntity.ok(
                adminService.getAllClients(status, search, pageable));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClientById(id));
    }

    @PutMapping("/client/{id}/block")
    public ResponseEntity<ClientResponse> blockClient(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockClient(id));
    }

    @PutMapping("/client/{id}/unblock")
    public ResponseEntity<ClientResponse> unblockClient(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.unblockClient(id));
    }


    @GetMapping("/sms")
    public ResponseEntity<Page<SmsResponse>> getAllSms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) SmsStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdDate").descending());
        return ResponseEntity.ok(
                smsService.getAllSms(clientId, phone, status, fromDate, toDate, pageable));
    }

    @GetMapping("/sms/{id}")
    public ResponseEntity<SmsResponse> getSmsById(@PathVariable Long id) {
        return ResponseEntity.ok(smsService.getSmsById(id));
    }


    @GetMapping("/transaction")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdDate").descending());
        return ResponseEntity.ok(
                transactionService.getAllTransactions(
                        clientId, type, fromDate, toDate, pageable));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }



    @GetMapping("/statistics/sms")
    public ResponseEntity<SmsStatisticsResponse> getSmsStatistics() {
        return ResponseEntity.ok(adminService.getSmsStatistics());
    }

    @GetMapping("/statistics/top-clients")
    public ResponseEntity<List<TopClientResponse>> getTopClients(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(adminService.getTopClients(limit));
    }
}
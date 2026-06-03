package dasturlash.uz.smsprovider.controller;

import dasturlash.uz.smsprovider.dto.request.ClientUpdateRequest;
import dasturlash.uz.smsprovider.dto.request.FillBalanceRequest;
import dasturlash.uz.smsprovider.dto.response.ClientResponse;
import dasturlash.uz.smsprovider.security.CustomUserDetails;
import dasturlash.uz.smsprovider.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/profile")
    public ResponseEntity<ClientResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(clientService.getById(userDetails.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<ClientResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ClientUpdateRequest request) {
        return ResponseEntity.ok(
                clientService.updateProfile(userDetails.getId(), request));
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                clientService.getById(userDetails.getId()).getBalance());
    }

    @PostMapping("/fill-balance")
    public ResponseEntity<ClientResponse> fillBalance(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FillBalanceRequest request) {
        return ResponseEntity.ok(
                clientService.fillBalance(userDetails.getId(), request));
    }
}
package dasturlash.uz.smsprovider.controller;

import dasturlash.uz.smsprovider.dto.request.ClientRegistrationRequest;
import dasturlash.uz.smsprovider.dto.request.ClientLoginRequest;
import dasturlash.uz.smsprovider.dto.request.ResetPasswordRequest;
import dasturlash.uz.smsprovider.dto.request.UpdatePasswordRequest;
import dasturlash.uz.smsprovider.dto.response.ClientLoginResponse;
import dasturlash.uz.smsprovider.dto.response.ClientResponse;
import dasturlash.uz.smsprovider.security.CustomUserDetails;
import dasturlash.uz.smsprovider.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/auth")
@RequiredArgsConstructor
public class ClientAuthController {

    private final ClientService clientService;

    @PostMapping("/registration")
    public ResponseEntity<ClientResponse> register(
            @Valid @RequestBody ClientRegistrationRequest request) {
        return ResponseEntity.ok(clientService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponse> login(
            @Valid @RequestBody ClientLoginRequest request) {
        return ResponseEntity.ok(clientService.login(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(clientService.resetPassword(request));
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest request) {
        return ResponseEntity.ok(
                clientService.updatePassword(userDetails.getId(), request));
    }
}
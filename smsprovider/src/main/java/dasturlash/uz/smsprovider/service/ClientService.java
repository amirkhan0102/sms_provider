package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.dto.request.*;
import dasturlash.uz.smsprovider.dto.response.ClientLoginResponse;
import dasturlash.uz.smsprovider.dto.response.ClientResponse;
import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.enums.TransactionType;
import dasturlash.uz.smsprovider.entity.TransactionEntity;
import dasturlash.uz.smsprovider.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import dasturlash.uz.smsprovider.dto.request.ResetPasswordRequest;


import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.exceptions.AppException;
import dasturlash.uz.smsprovider.repository.ClientRepository;
import dasturlash.uz.smsprovider.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TransactionRepository transactionRepository;

    public ClientResponse register(ClientRegistrationRequest request) {
        if (clientRepository.existsByLogin(request.getLogin())) {
            throw new AppException("Bu login allaqachon band: " + request.getLogin(),
                    HttpStatus.CONFLICT.value());
        }
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Bu email allaqachon ro'yxatdan o'tgan: " + request.getEmail(),
                    HttpStatus.CONFLICT.value());
        }

        ClientEntity client = ClientEntity.builder()
                .companyName(request.getCompanyName())
                .ownerName(request.getOwnerName())
                .ownerSurname(request.getOwnerSurname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        clientRepository.save(client);
        return toResponse(client);
    }

    public ClientLoginResponse login(ClientLoginRequest request) {
        ClientEntity client = clientRepository.findByLoginAndVisibleTrue(request.getLogin())
                .orElseThrow(() -> new AppException("Login yoki parol noto'g'ri",
                        HttpStatus.UNAUTHORIZED.value()));

        if (client.getStatus() == GeneralStatus.BLOCKED) {
            throw new AppException("Akkaunt bloklangan",
                    HttpStatus.FORBIDDEN.value());
        }

        if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
            throw new AppException("Login yoki parol noto'g'ri",
                    HttpStatus.UNAUTHORIZED.value());
        }

        String token = jwtUtil.generateToken(
                client.getId(),
                client.getLogin(),
                "CLIENT");

        return ClientLoginResponse.builder()
                .id(client.getId())
                .companyName(client.getCompanyName())
                .login(client.getLogin())
                .token(token)
                .build();
    }

    public ClientResponse updateProfile(Long clientId, ClientUpdateRequest request) {
        ClientEntity clientEntity = getClientOrThrow(clientId);
        if (!clientEntity.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Bu email allaqachon ishlatilmoqda", HttpStatus.CONFLICT.value());
        }

        clientEntity.setCompanyName(request.getCompanyName());
        clientEntity.setOwnerName(request.getOwnerName());
        clientEntity.setOwnerSurname(request.getOwnerSurname());
        clientEntity.setPhone(request.getPhone());
        clientEntity.setEmail(request.getEmail());

        return toResponse(clientRepository.save(clientEntity));
    }

    public String updatePassword(Long clientId, UpdatePasswordRequest request) {
        ClientEntity client = getClientOrThrow(clientId);

        if (!passwordEncoder.matches(request.getOldPassword(), client.getPassword())) {
            throw new AppException("Eski parol noto'g'ri",
                    HttpStatus.BAD_REQUEST.value());
        }

        client.setPassword(passwordEncoder.encode(request.getNewPassword()));
        clientRepository.save(client);
        return "Parol muvaffaqiyatli yangilandi";
    }
    public String resetPassword(ResetPasswordRequest request) {
        ClientEntity client = clientRepository.findByLoginAndVisibleTrue(request.getLogin())
                .orElseThrow(() -> new AppException("Client topilmadi",
                        HttpStatus.NOT_FOUND.value()));

        client.setPassword(passwordEncoder.encode(request.getNewPassword()));
        clientRepository.save(client);
        return "Parol muvaffaqiyatli yangilandi";
    }

    @Transactional
    public ClientResponse fillBalance(Long clientId, FillBalanceRequest request) {
        ClientEntity client = getClientOrThrow(clientId);

        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        client.setBalance(balanceAfter);
        clientRepository.save(client);

        TransactionEntity transaction = TransactionEntity.builder()
                .clientId(clientId)
                .type(TransactionType.CREDIT)
                .amount(request.getAmount())
                .description("Balans to'ldirildi: " + request.getAmount() + " so'm")
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();
        transactionRepository.save(transaction);

        return toResponse(client);
    }

    public ClientResponse getById(Long id) {
        return toResponse(getClientOrThrow(id));
    }

    public ClientEntity getClientOrThrow(Long id) {
        return clientRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppException("Client topilmadi: " + id,
                        HttpStatus.NOT_FOUND.value()));
    }

    public ClientResponse toResponse(ClientEntity client) {
        return ClientResponse.builder()
                .id(client.getId())
                .companyName(client.getCompanyName())
                .ownerName(client.getOwnerName())
                .ownerSurname(client.getOwnerSurname())
                .phone(client.getPhone())
                .email(client.getEmail())
                .login(client.getLogin())
                .balance(client.getBalance())
                .status(client.getStatus())
                .createdDate(client.getCreatedDate())
                .build();
    }
}
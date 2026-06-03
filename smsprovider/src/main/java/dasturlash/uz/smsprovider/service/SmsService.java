package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.dto.request.SmsSendRequest;
import dasturlash.uz.smsprovider.dto.response.SmsResponse;
import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.entity.SmsEntity;
import dasturlash.uz.smsprovider.entity.TransactionEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.enums.SmsStatus;
import dasturlash.uz.smsprovider.enums.TransactionType;
import dasturlash.uz.smsprovider.exceptions.AppException;
import dasturlash.uz.smsprovider.repository.ClientRepository;
import dasturlash.uz.smsprovider.repository.SmsRepository;
import dasturlash.uz.smsprovider.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final BigDecimal SMS_PRICE = new BigDecimal("125");

    private final SmsRepository smsRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public SmsResponse sendSms(SmsSendRequest request) {
        // 1. Clientni topish
        ClientEntity client = clientRepository.findByIdAndVisibleTrue(request.getClientId())
                .orElseThrow(() -> new AppException("Client topilmadi",
                        HttpStatus.NOT_FOUND.value()));

        // 2. Client ACTIVE bo'lishi kerak
        if (client.getStatus() != GeneralStatus.ACTIVE) {
            throw new AppException("Client bloklangan, SMS yuborib bo'lmaydi",
                    HttpStatus.FORBIDDEN.value());
        }

        // 3. Balance yetarliligini tekshirish
        if (client.getBalance().compareTo(SMS_PRICE) < 0) {
            throw new AppException(
                    "Balans yetarli emas. Joriy balans: " + client.getBalance() +
                            " so'm. SMS narxi: " + SMS_PRICE + " so'm",
                    HttpStatus.PAYMENT_REQUIRED.value());
        }

        // 4. SMS saqlash
        SmsEntity sms = SmsEntity.builder()
                .clientId(client.getId())
                .phone(request.getPhone())
                .text(request.getText())
                .price(SMS_PRICE)
                .status(SmsStatus.SENT)
                .build();
        smsRepository.save(sms);

        // 5. Balansdan yechish
        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(SMS_PRICE);
        client.setBalance(balanceAfter);
        clientRepository.save(client);

        // 6. Transaction yozish
        TransactionEntity transaction = TransactionEntity.builder()
                .clientId(client.getId())
                .type(TransactionType.DEBIT)
                .amount(SMS_PRICE)
                .description("SMS yuborildi. Telefon: " + request.getPhone())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();
        transactionRepository.save(transaction);

        return toResponse(sms);
    }

    public Page<SmsResponse> getMySms(Long clientId, LocalDateTime fromDate,
                                      LocalDateTime toDate, Pageable pageable) {
        return smsRepository.findByClientIdWithFilter(
                        clientId, fromDate, toDate, pageable)
                .map(this::toResponse);
    }

    public SmsResponse getSmsById(Long id) {
        return smsRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new AppException("SMS topilmadi",
                        HttpStatus.NOT_FOUND.value()));
    }

    public Page<SmsResponse> getAllSms(Long clientId, String phone,
                                       dasturlash.uz.smsprovider.enums.SmsStatus status,
                                       LocalDateTime fromDate, LocalDateTime toDate,
                                       Pageable pageable) {
        return smsRepository.findAllWithFilter(
                        clientId, phone, status, fromDate, toDate, pageable)
                .map(this::toResponse);
    }

    private SmsResponse toResponse(SmsEntity sms) {
        return SmsResponse.builder()
                .id(sms.getId())
                .clientId(sms.getClientId())
                .phone(sms.getPhone())
                .text(sms.getText())
                .price(sms.getPrice())
                .status(sms.getStatus())
                .createdDate(sms.getCreatedDate())
                .build();
    }
}
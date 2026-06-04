package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.entity.SmsEntity;
import dasturlash.uz.smsprovider.entity.TransactionEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.enums.SmsStatus;
import dasturlash.uz.smsprovider.enums.TransactionType;
import dasturlash.uz.smsprovider.repository.ClientRepository;
import dasturlash.uz.smsprovider.repository.SmsRepository;
import dasturlash.uz.smsprovider.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsScheduler {

    private static final BigDecimal SMS_PRICE = new BigDecimal("125");

    private final SmsRepository smsRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Scheduled(fixedRate = 60000) // har 60 sekund
    @Transactional
    public void processPendingSms() {
        log.info("Pending SMS lar tekshirilmoqda...");

        // Vaqti yetgan PENDING SMS larni olamiz
        List<SmsEntity> pendingSmsList = smsRepository
                .findPendingScheduledSms(LocalDateTime.now());

        if (pendingSmsList.isEmpty()) {
            log.info("Pending SMS topilmadi");
            return;
        }

        log.info("{} ta pending SMS topildi", pendingSmsList.size());

        for (SmsEntity sms : pendingSmsList) {
            try {
                processSingleSms(sms);
            } catch (Exception e) {
                log.error("SMS yuborishda xato. SMS id: {}, xato: {}",
                        sms.getId(), e.getMessage());
                sms.setStatus(SmsStatus.FAILED);
                smsRepository.save(sms);
            }
        }
    }

    @Transactional
    public void processSingleSms(SmsEntity sms) {
        ClientEntity client = clientRepository
                .findByIdAndVisibleTrue(sms.getClientId())
                .orElseThrow(() -> new RuntimeException("Client topilmadi"));

        // Client bloklangan bo'lsa
        if (client.getStatus() != GeneralStatus.ACTIVE) {
            sms.setStatus(SmsStatus.FAILED);
            smsRepository.save(sms);
            log.warn("Client bloklangan. SMS id: {}", sms.getId());
            return;
        }

        // Balance yetarli emas
        if (client.getBalance().compareTo(SMS_PRICE) < 0) {
            sms.setStatus(SmsStatus.FAILED);
            smsRepository.save(sms);
            log.warn("Balans yetarli emas. SMS id: {}", sms.getId());
            return;
        }

        // Balansdan yechish
        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(SMS_PRICE);
        client.setBalance(balanceAfter);
        clientRepository.save(client);

        // Transaction yozish
        TransactionEntity transaction = TransactionEntity.builder()
                .clientId(client.getId())
                .type(TransactionType.DEBIT)
                .amount(SMS_PRICE)
                .description("Scheduled SMS yuborildi. Telefon: " + sms.getPhone())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();
        transactionRepository.save(transaction);

        // SMS SENT ga o'zgartirish
        sms.setStatus(SmsStatus.SENT);
        smsRepository.save(sms);

        log.info("SMS muvaffaqiyatli yuborildi. SMS id: {}", sms.getId());
    }
}
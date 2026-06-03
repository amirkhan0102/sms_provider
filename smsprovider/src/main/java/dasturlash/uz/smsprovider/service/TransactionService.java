package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.dto.response.TransactionResponse;
import dasturlash.uz.smsprovider.entity.TransactionEntity;
import dasturlash.uz.smsprovider.enums.TransactionType;
import dasturlash.uz.smsprovider.exceptions.AppException;
import dasturlash.uz.smsprovider.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Page<TransactionResponse> getMyTransactions(Long clientId,
                                                       TransactionType type,
                                                       LocalDateTime fromDate,
                                                       LocalDateTime toDate,
                                                       Pageable pageable) {
        return transactionRepository
                .findByClientIdWithFilter(clientId, type, fromDate, toDate, pageable)
                .map(this::toResponse);
    }

    public Page<TransactionResponse> getAllTransactions(Long clientId,
                                                        TransactionType type,
                                                        LocalDateTime fromDate,
                                                        LocalDateTime toDate,
                                                        Pageable pageable) {
        return transactionRepository
                .findAllWithFilter(clientId, type, fromDate, toDate, pageable)
                .map(this::toResponse);
    }

    public TransactionResponse getById(Long id) {
        return transactionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new AppException("Transaction topilmadi",
                        HttpStatus.NOT_FOUND.value()));
    }

    private TransactionResponse toResponse(TransactionEntity t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .clientId(t.getClientId())
                .type(t.getType())
                .amount(t.getAmount())
                .description(t.getDescription())
                .balanceBefore(t.getBalanceBefore())
                .balanceAfter(t.getBalanceAfter())
                .createdDate(t.getCreatedDate())
                .build();
    }
}
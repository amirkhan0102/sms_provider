package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.dto.response.ClientResponse;
import dasturlash.uz.smsprovider.dto.response.SmsStatisticsResponse;
import dasturlash.uz.smsprovider.dto.response.TopClientResponse;
import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.exceptions.AppException;
import dasturlash.uz.smsprovider.repository.ClientRepository;
import dasturlash.uz.smsprovider.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ClientRepository clientRepository;
    private final SmsRepository smsRepository;
    private final ClientService clientService;

    public Page<ClientResponse> getAllClients(GeneralStatus status,
                                              String search,
                                              Pageable pageable) {
        return clientRepository.findAllWithFilter(status, search, pageable)
                .map(clientService::toResponse);
    }

    public ClientResponse getClientById(Long id) {
        return clientService.toResponse(clientService.getClientOrThrow(id));
    }

    public ClientResponse blockClient(Long id) {
        ClientEntity client = clientService.getClientOrThrow(id);
        if (client.getStatus() == GeneralStatus.BLOCKED) {
            throw new AppException("Client allaqachon bloklangan",
                    HttpStatus.BAD_REQUEST.value());
        }
        client.setStatus(GeneralStatus.BLOCKED);
        return clientService.toResponse(clientRepository.save(client));
    }

    public ClientResponse unblockClient(Long id) {
        ClientEntity client = clientService.getClientOrThrow(id);
        if (client.getStatus() == GeneralStatus.ACTIVE) {
            throw new AppException("Client allaqachon aktiv",
                    HttpStatus.BAD_REQUEST.value());
        }
        client.setStatus(GeneralStatus.ACTIVE);
        return clientService.toResponse(clientRepository.save(client));
    }

    public SmsStatisticsResponse getSmsStatistics() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return SmsStatisticsResponse.builder()
                .todaySmsCount(smsRepository.countTodaySms(startOfDay))
                .totalSmsCount(smsRepository.count())
                .todayIncome(smsRepository.sumTodayIncome(startOfDay))
                .totalIncome(smsRepository.sumTotalIncome())
                .build();
    }

    public List<TopClientResponse> getTopClients(int limit) {
        return smsRepository.findTopClients(PageRequest.of(0, limit));
    }
}
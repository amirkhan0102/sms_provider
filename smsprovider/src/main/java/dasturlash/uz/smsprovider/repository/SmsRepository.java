package dasturlash.uz.smsprovider.repository;

import dasturlash.uz.smsprovider.dto.response.TopClientResponse;
import dasturlash.uz.smsprovider.entity.SmsEntity;
import dasturlash.uz.smsprovider.enums.SmsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SmsRepository extends JpaRepository<SmsEntity, Long> {

    @Query("SELECT s FROM SmsEntity s WHERE s.clientId = :clientId " +
            "AND (CAST(:fromDate AS java.time.LocalDateTime) IS NULL OR s.createdDate >= :fromDate) " +
            "AND (CAST(:toDate AS java.time.LocalDateTime) IS NULL OR s.createdDate <= :toDate)")
    Page<SmsEntity> findByClientIdWithFilter(
            @Param("clientId") Long clientId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);

    @Query("SELECT s FROM SmsEntity s WHERE " +
            "(:clientId IS NULL OR s.clientId = :clientId) " +
            "AND (CAST(:phone AS string) IS NULL OR s.phone LIKE CONCAT('%', CAST(:phone AS string), '%')) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (CAST(:fromDate AS java.time.LocalDateTime) IS NULL OR s.createdDate >= :fromDate) " +
            "AND (CAST(:toDate AS java.time.LocalDateTime) IS NULL OR s.createdDate <= :toDate)")
    Page<SmsEntity> findAllWithFilter(
            @Param("clientId") Long clientId,
            @Param("phone") String phone,
            @Param("status") SmsStatus status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);

    @Query("SELECT COUNT(s) FROM SmsEntity s WHERE s.createdDate >= :startOfDay")
    Long countTodaySms(@Param("startOfDay") LocalDateTime startOfDay);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM SmsEntity s WHERE s.createdDate >= :startOfDay")
    BigDecimal sumTodayIncome(@Param("startOfDay") LocalDateTime startOfDay);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM SmsEntity s")
    BigDecimal sumTotalIncome();

    @Query("SELECT new dasturlash.uz.smsprovider.dto.response.TopClientResponse(" +
            "s.clientId, c.companyName, COUNT(s)) " +
            "FROM SmsEntity s JOIN ClientEntity c ON c.id = s.clientId " +
            "GROUP BY s.clientId, c.companyName " +
            "ORDER BY COUNT(s) DESC")
    List<TopClientResponse> findTopClients(Pageable pageable);

    @Query("SELECT s FROM SmsEntity s WHERE s.status = 'PENDING' " +
            "AND s.scheduledDate IS NOT NULL " +
            "AND s.scheduledDate <= :now")
    List<SmsEntity> findPendingScheduledSms(@Param("now") LocalDateTime now);
}
package dasturlash.uz.smsprovider.repository;

import dasturlash.uz.smsprovider.entity.TransactionEntity;
import dasturlash.uz.smsprovider.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.clientId = :clientId " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (CAST(:fromDate AS java.time.LocalDateTime) IS NULL OR t.createdDate >= :fromDate) " +
            "AND (CAST(:toDate AS java.time.LocalDateTime) IS NULL OR t.createdDate <= :toDate)")
    Page<TransactionEntity> findByClientIdWithFilter(
            @Param("clientId") Long clientId,
            @Param("type") TransactionType type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "(:clientId IS NULL OR t.clientId = :clientId) " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (CAST(:fromDate AS java.time.LocalDateTime) IS NULL OR t.createdDate >= :fromDate) " +
            "AND (CAST(:toDate AS java.time.LocalDateTime) IS NULL OR t.createdDate <= :toDate)")
    Page<TransactionEntity> findAllWithFilter(
            @Param("clientId") Long clientId,
            @Param("type") TransactionType type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);
}
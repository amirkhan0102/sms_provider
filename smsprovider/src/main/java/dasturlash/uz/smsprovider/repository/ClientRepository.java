package dasturlash.uz.smsprovider.repository;

import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByLoginAndVisibleTrue(String login);

    Optional<ClientEntity> findByIdAndVisibleTrue(Long id);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM ClientEntity c WHERE c.visible = true " +
            "AND (CAST(:status AS string) IS NULL OR c.status = :status) " +
            "AND (CAST(:search AS string) IS NULL OR " +
            "LOWER(c.companyName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) " +
            "OR LOWER(c.ownerName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) " +
            "OR LOWER(c.login) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
    Page<ClientEntity> findAllWithFilter(
            @Param("status") GeneralStatus status,
            @Param("search") String search,
            Pageable pageable);
}
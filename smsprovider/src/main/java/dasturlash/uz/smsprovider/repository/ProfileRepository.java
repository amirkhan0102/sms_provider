package dasturlash.uz.smsprovider.repository;

import dasturlash.uz.smsprovider.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByLoginAndVisibleTrue(String login);

    boolean existsByLogin(String login);

}
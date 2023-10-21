package shop.mookmall.com.model.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUuidAndStatus(String uuid, TokenStatus status);

    Optional<RefreshTokenEntity> findByUuid(String uuid);

    List<RefreshTokenEntity> findByStatus(TokenStatus status);
}

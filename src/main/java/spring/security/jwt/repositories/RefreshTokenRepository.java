package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.jwt.model.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findRefreshTokenByToken(String token);

    Optional<RefreshToken> findRefreshTokenByUser_Id(Integer id);

}

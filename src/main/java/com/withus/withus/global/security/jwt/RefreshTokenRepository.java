package com.withus.withus.global.security.jwt;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByKeyUsername(String username);

  void deleteByKeyUsername(String username);

  boolean existsByKeyUsername(String username);


}

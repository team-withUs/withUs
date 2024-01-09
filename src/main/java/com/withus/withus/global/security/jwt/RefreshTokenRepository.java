package com.withus.withus.global.security.jwt;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByKeyLoginname(String loginname);

  void deleteByKeyLoginname(String loginname);

  boolean existsByKeyLoginname(String loginname);


}

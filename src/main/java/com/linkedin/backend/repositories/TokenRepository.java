package com.linkedin.backend.repositories;

import com.linkedin.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
        Optional<Token> findByToken(String token);

}

package com.example.code.repositories;

import com.example.code.entities.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long>
{
    Optional<ActivationCode> findFirstByTariffIdAndIsActive(long tariffId, Boolean isActive);
}

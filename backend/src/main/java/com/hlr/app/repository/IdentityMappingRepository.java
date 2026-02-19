package com.hlr.app.repository;

import com.hlr.app.entity.IdentityMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityMappingRepository extends JpaRepository<IdentityMapping, UUID> {
    Optional<IdentityMapping> findByImsiAndActiveTrue(String imsi);
    Optional<IdentityMapping> findByMsisdnAndActiveTrue(String msisdn);
    List<IdentityMapping> findBySubscriberId(UUID subscriberId);
}

package com.hlr.app.repository;

import com.hlr.app.entity.RoamingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoamingPolicyRepository extends JpaRepository<RoamingPolicy, UUID> {
}

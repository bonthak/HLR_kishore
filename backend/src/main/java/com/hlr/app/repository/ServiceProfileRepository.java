package com.hlr.app.repository;

import com.hlr.app.entity.ServiceProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceProfileRepository extends JpaRepository<ServiceProfile, UUID> {
}

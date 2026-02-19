package com.hlr.app.repository;

import com.hlr.app.entity.ChangeRequest;
import com.hlr.app.entity.ChangeRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, UUID> {
    List<ChangeRequest> findByStatusOrderByRequestedAtDesc(ChangeRequestStatus status);
}

package com.hlr.app.service;

import com.hlr.app.dto.ApprovalRequest;
import com.hlr.app.dto.CreateChangeRequest;
import com.hlr.app.entity.ChangeRequest;
import com.hlr.app.entity.ChangeRequestStatus;
import com.hlr.app.repository.ChangeRequestRepository;
import com.hlr.app.repository.SubscriberRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChangeRequestService {
    private final ChangeRequestRepository changeRequestRepository;
    private final SubscriberRepository subscriberRepository;
    private final AuditService auditService;

    public ChangeRequestService(ChangeRequestRepository changeRequestRepository,
                                SubscriberRepository subscriberRepository,
                                AuditService auditService) {
        this.changeRequestRepository = changeRequestRepository;
        this.subscriberRepository = subscriberRepository;
        this.auditService = auditService;
    }

    @Transactional
    public ChangeRequest create(CreateChangeRequest request) {
        ChangeRequest cr = new ChangeRequest();
        cr.operationType = request.operationType;
        cr.riskLevel = request.riskLevel;
        cr.status = ChangeRequestStatus.PENDING;
        cr.requestedBy = request.requestedBy;
        cr.payload = request.payload;
        cr.correlationId = UUID.randomUUID().toString();

        if (request.subscriberId != null) {
            cr.subscriber = subscriberRepository.findById(request.subscriberId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));
        }

        ChangeRequest saved = changeRequestRepository.save(cr);
        auditService.log(request.requestedBy, "CREATE_CHANGE_REQUEST", "CHANGE_REQUEST", saved.id.toString(), null, saved.status.name(), saved.correlationId);
        return saved;
    }

    @Transactional
    public ChangeRequest approve(UUID id, ApprovalRequest request) {
        ChangeRequest cr = changeRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Change request not found"));

        if (cr.requestedBy.equalsIgnoreCase(request.approver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Self-approval is not allowed");
        }

        ChangeRequestStatus oldStatus = cr.status;
        cr.approvedBy = request.approver;
        cr.approvedAt = OffsetDateTime.now();
        cr.status = request.approved ? ChangeRequestStatus.APPROVED : ChangeRequestStatus.REJECTED;

        ChangeRequest saved = changeRequestRepository.save(cr);
        auditService.log(request.approver, "APPROVE_CHANGE_REQUEST", "CHANGE_REQUEST", id.toString(), oldStatus.name(), saved.status.name(), saved.correlationId);
        return saved;
    }

    public List<ChangeRequest> listPending() {
        return changeRequestRepository.findByStatusOrderByRequestedAtDesc(ChangeRequestStatus.PENDING);
    }
}

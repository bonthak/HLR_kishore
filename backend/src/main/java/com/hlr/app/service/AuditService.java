package com.hlr.app.service;

import com.hlr.app.entity.AuditLog;
import com.hlr.app.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String actor, String action, String entityType, String entityId, String beforeState, String afterState, String correlationId) {
        AuditLog auditLog = new AuditLog();
        auditLog.actor = actor;
        auditLog.action = action;
        auditLog.entityType = entityType;
        auditLog.entityId = entityId;
        auditLog.beforeState = beforeState;
        auditLog.afterState = afterState;
        auditLog.correlationId = correlationId;
        auditLogRepository.save(auditLog);
    }
}

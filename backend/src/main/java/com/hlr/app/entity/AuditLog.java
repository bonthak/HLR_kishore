package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Column(nullable = false)
    public String actor;

    @Column(nullable = false)
    public String action;

    @Column(nullable = false)
    public String entityType;

    @Column(nullable = false)
    public String entityId;

    @Column(columnDefinition = "TEXT")
    public String beforeState;

    @Column(columnDefinition = "TEXT")
    public String afterState;

    @Column(nullable = false)
    public String correlationId;

    @Column(nullable = false)
    public OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
    }
}

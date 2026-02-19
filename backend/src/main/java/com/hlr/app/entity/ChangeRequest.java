package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "change_requests")
public class ChangeRequest {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public OperationType operationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ChangeRequestStatus status;

    @Column(nullable = false)
    public String requestedBy;

    public String approvedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String payload;

    @Column(nullable = false)
    public String correlationId;

    @Column(nullable = false)
    public OffsetDateTime requestedAt;

    public OffsetDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    public Subscriber subscriber;

    @PrePersist
    void prePersist() {
        requestedAt = OffsetDateTime.now();
    }
}

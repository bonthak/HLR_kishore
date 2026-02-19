package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "identity_mappings")
public class IdentityMapping {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Column(nullable = false, unique = true)
    public String imsi;

    @Column(nullable = false, unique = true)
    public String msisdn;

    @Column(nullable = false, unique = true)
    public String iccid;

    @Column(nullable = false)
    public boolean active;

    @Column(nullable = false)
    public OffsetDateTime effectiveFrom;

    public OffsetDateTime effectiveTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    public Subscriber subscriber;
}

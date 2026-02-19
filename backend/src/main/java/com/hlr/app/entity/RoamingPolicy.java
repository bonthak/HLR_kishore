package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "roaming_policies")
public class RoamingPolicy {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Column(nullable = false)
    public boolean roamingEnabled;

    @Column(nullable = false)
    public String allowedVplmnCsv;

    @Column(nullable = false)
    public String barredVplmnCsv;

    @Column(nullable = false)
    public String regionClass;

    public LocalDate effectiveFrom;

    public LocalDate effectiveTo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false, unique = true)
    public Subscriber subscriber;
}

package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "service_profiles")
public class ServiceProfile {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Column(nullable = false)
    public boolean baoc;

    @Column(nullable = false)
    public boolean boic;

    @Column(nullable = false)
    public boolean baic;

    @Column(nullable = false)
    public boolean clip;

    @Column(nullable = false)
    public boolean clir;

    @Column(nullable = false)
    public boolean callWaiting;

    @Column(nullable = false)
    public boolean gprsEnabled;

    @Column(nullable = false)
    public String apnProfile;

    @Column(nullable = false)
    public String camelProfileVersion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false, unique = true)
    public Subscriber subscriber;
}

package com.hlr.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue
    @UuidGenerator
    public UUID id;

    @Column(nullable = false, unique = true)
    public String externalRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public SubscriberState state;

    @Column(nullable = false)
    public String market;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<IdentityMapping> identities = new ArrayList<>();

    @OneToOne(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    public ServiceProfile serviceProfile;

    @OneToOne(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    public RoamingPolicy roamingPolicy;

    @Column(nullable = false)
    public OffsetDateTime createdAt;

    @Column(nullable = false)
    public OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}

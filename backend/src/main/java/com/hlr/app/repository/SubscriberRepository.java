package com.hlr.app.repository;

import com.hlr.app.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByExternalRef(String externalRef);
}

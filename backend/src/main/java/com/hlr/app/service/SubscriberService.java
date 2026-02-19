package com.hlr.app.service;

import com.hlr.app.dto.CreateSubscriberRequest;
import com.hlr.app.dto.NumberChangeRequest;
import com.hlr.app.dto.SimSwapRequest;
import com.hlr.app.dto.SubscriberResponse;
import com.hlr.app.entity.*;
import com.hlr.app.repository.IdentityMappingRepository;
import com.hlr.app.repository.SubscriberRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final IdentityMappingRepository identityMappingRepository;
    private final AuditService auditService;

    public SubscriberService(SubscriberRepository subscriberRepository,
                             IdentityMappingRepository identityMappingRepository,
                             AuditService auditService) {
        this.subscriberRepository = subscriberRepository;
        this.identityMappingRepository = identityMappingRepository;
        this.auditService = auditService;
    }

    @Transactional
    public SubscriberResponse create(CreateSubscriberRequest request, String actor) {
        subscriberRepository.findByExternalRef(request.externalRef).ifPresent(x -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "externalRef already exists");
        });

        if (identityMappingRepository.findByImsiAndActiveTrue(request.imsi).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IMSI already active");
        }
        if (identityMappingRepository.findByMsisdnAndActiveTrue(request.msisdn).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MSISDN already active");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.externalRef = request.externalRef;
        subscriber.market = request.market;
        subscriber.state = SubscriberState.INACTIVE;

        IdentityMapping mapping = new IdentityMapping();
        mapping.imsi = request.imsi;
        mapping.msisdn = request.msisdn;
        mapping.iccid = request.iccid;
        mapping.active = true;
        mapping.effectiveFrom = OffsetDateTime.now();
        mapping.subscriber = subscriber;
        subscriber.identities.add(mapping);

        ServiceProfile profile = new ServiceProfile();
        profile.baoc = request.baoc;
        profile.boic = request.boic;
        profile.baic = request.baic;
        profile.clip = request.clip;
        profile.clir = request.clir;
        profile.callWaiting = request.callWaiting;
        profile.gprsEnabled = request.gprsEnabled;
        profile.apnProfile = request.apnProfile;
        profile.camelProfileVersion = request.camelProfileVersion;
        profile.subscriber = subscriber;
        subscriber.serviceProfile = profile;

        RoamingPolicy roamingPolicy = new RoamingPolicy();
        roamingPolicy.roamingEnabled = request.roamingEnabled;
        roamingPolicy.allowedVplmnCsv = request.allowedVplmnCsv;
        roamingPolicy.barredVplmnCsv = request.barredVplmnCsv;
        roamingPolicy.regionClass = request.regionClass;
        roamingPolicy.subscriber = subscriber;
        subscriber.roamingPolicy = roamingPolicy;

        Subscriber saved = subscriberRepository.save(subscriber);
        String correlationId = UUID.randomUUID().toString();
        auditService.log(actor, "CREATE_SUBSCRIBER", "SUBSCRIBER", saved.id.toString(), null, saved.externalRef, correlationId);

        return toResponse(saved);
    }

    public SubscriberResponse getById(UUID id) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));
        return toResponse(subscriber);
    }

    public List<SubscriberResponse> search(String key) {
        return subscriberRepository.findAll().stream()
                .filter(s -> s.externalRef.equalsIgnoreCase(key)
                        || s.identities.stream().anyMatch(i -> i.imsi.equals(key) || i.msisdn.equals(key) || i.iccid.equals(key)))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SubscriberResponse updateState(UUID subscriberId, SubscriberState state, String actor) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));

        SubscriberState oldState = subscriber.state;
        subscriber.state = state;
        Subscriber saved = subscriberRepository.save(subscriber);

        auditService.log(actor, "STATE_CHANGE", "SUBSCRIBER", subscriberId.toString(), oldState.name(), state.name(), UUID.randomUUID().toString());
        return toResponse(saved);
    }

    @Transactional
    public SubscriberResponse simSwap(UUID subscriberId, SimSwapRequest request) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));

        if (identityMappingRepository.findByImsiAndActiveTrue(request.newImsi).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New IMSI already active");
        }

        IdentityMapping oldMapping = subscriber.identities.stream()
                .filter(i -> i.active)
                .filter(i -> i.imsi.equals(request.oldImsi))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old IMSI is not active for this subscriber"));

        oldMapping.active = false;
        oldMapping.effectiveTo = OffsetDateTime.now();

        IdentityMapping newMapping = new IdentityMapping();
        newMapping.imsi = request.newImsi;
        newMapping.msisdn = oldMapping.msisdn;
        newMapping.iccid = request.newIccid;
        newMapping.active = true;
        newMapping.effectiveFrom = OffsetDateTime.now();
        newMapping.subscriber = subscriber;
        subscriber.identities.add(newMapping);

        Subscriber saved = subscriberRepository.save(subscriber);
        auditService.log(request.actor, "SIM_SWAP", "SUBSCRIBER", subscriberId.toString(), request.oldImsi, request.newImsi, UUID.randomUUID().toString());
        return toResponse(saved);
    }

    @Transactional
    public SubscriberResponse numberChange(UUID subscriberId, NumberChangeRequest request) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));

        if (identityMappingRepository.findByMsisdnAndActiveTrue(request.newMsisdn).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New MSISDN already active");
        }

        IdentityMapping oldMapping = subscriber.identities.stream()
                .filter(i -> i.active)
                .filter(i -> i.msisdn.equals(request.oldMsisdn))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old MSISDN is not active for this subscriber"));

        oldMapping.active = false;
        oldMapping.effectiveTo = OffsetDateTime.now();

        IdentityMapping newMapping = new IdentityMapping();
        newMapping.imsi = oldMapping.imsi;
        newMapping.msisdn = request.newMsisdn;
        newMapping.iccid = oldMapping.iccid;
        newMapping.active = true;
        newMapping.effectiveFrom = OffsetDateTime.now();
        newMapping.subscriber = subscriber;
        subscriber.identities.add(newMapping);

        Subscriber saved = subscriberRepository.save(subscriber);
        auditService.log(request.actor, "NUMBER_CHANGE", "SUBSCRIBER", subscriberId.toString(), request.oldMsisdn, request.newMsisdn, UUID.randomUUID().toString());
        return toResponse(saved);
    }

    private SubscriberResponse toResponse(Subscriber subscriber) {
        IdentityMapping active = subscriber.identities.stream().filter(i -> i.active).findFirst().orElse(null);
        SubscriberResponse response = new SubscriberResponse();
        response.id = subscriber.id;
        response.externalRef = subscriber.externalRef;
        response.market = subscriber.market;
        response.state = subscriber.state;
        response.activeImsi = active != null ? active.imsi : null;
        response.activeMsisdn = active != null ? active.msisdn : null;
        response.iccid = active != null ? active.iccid : null;
        response.roamingEnabled = subscriber.roamingPolicy != null && subscriber.roamingPolicy.roamingEnabled;
        return response;
    }
}

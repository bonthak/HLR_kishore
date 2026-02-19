package com.hlr.app.controller;

import com.hlr.app.dto.CreateSubscriberRequest;
import com.hlr.app.dto.NumberChangeRequest;
import com.hlr.app.dto.SimSwapRequest;
import com.hlr.app.dto.SubscriberResponse;
import com.hlr.app.entity.SubscriberState;
import com.hlr.app.service.SubscriberService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROVISIONER','ADMIN')")
    public SubscriberResponse create(@Valid @RequestBody CreateSubscriberRequest request,
                                     @RequestHeader(name = "X-Actor", defaultValue = "system") String actor) {
        return subscriberService.create(request, actor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','PROVISIONER','APPROVER','NOC_LEAD','AUDITOR','ADMIN')")
    public SubscriberResponse getById(@PathVariable UUID id) {
        return subscriberService.getById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('VIEWER','PROVISIONER','APPROVER','NOC_LEAD','AUDITOR','ADMIN')")
    public List<SubscriberResponse> search(@RequestParam("key") String key) {
        return subscriberService.search(key);
    }

    @PostMapping("/{id}/sim-swap")
    @PreAuthorize("hasAnyRole('PROVISIONER','ADMIN')")
    public SubscriberResponse simSwap(@PathVariable UUID id, @Valid @RequestBody SimSwapRequest request) {
        return subscriberService.simSwap(id, request);
    }

    @PostMapping("/{id}/number-change")
    @PreAuthorize("hasAnyRole('PROVISIONER','ADMIN')")
    public SubscriberResponse numberChange(@PathVariable UUID id, @Valid @RequestBody NumberChangeRequest request) {
        return subscriberService.numberChange(id, request);
    }

    @PostMapping("/{id}/state/{state}")
    @PreAuthorize("hasAnyRole('PROVISIONER','NOC_LEAD','ADMIN')")
    public SubscriberResponse stateChange(@PathVariable UUID id,
                                          @PathVariable SubscriberState state,
                                          @RequestHeader(name = "X-Actor", defaultValue = "system") String actor) {
        return subscriberService.updateState(id, state, actor);
    }
}

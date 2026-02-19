package com.hlr.app.controller;

import com.hlr.app.dto.ApprovalRequest;
import com.hlr.app.dto.CreateChangeRequest;
import com.hlr.app.entity.ChangeRequest;
import com.hlr.app.service.ChangeRequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/change-requests")
public class ChangeRequestController {
    private final ChangeRequestService changeRequestService;

    public ChangeRequestController(ChangeRequestService changeRequestService) {
        this.changeRequestService = changeRequestService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROVISIONER','NOC_LEAD','ADMIN')")
    public ChangeRequest create(@Valid @RequestBody CreateChangeRequest request) {
        return changeRequestService.create(request);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('APPROVER','AUDITOR','ADMIN')")
    public List<ChangeRequest> pending() {
        return changeRequestService.listPending();
    }

    @PostMapping("/{id}/approval")
    @PreAuthorize("hasAnyRole('APPROVER','ADMIN')")
    public ChangeRequest approval(@PathVariable UUID id, @Valid @RequestBody ApprovalRequest request) {
        return changeRequestService.approve(id, request);
    }
}

package com.hlr.app.dto;

import com.hlr.app.entity.OperationType;
import com.hlr.app.entity.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateChangeRequest {
    public UUID subscriberId;
    @NotNull
    public OperationType operationType;
    @NotNull
    public RiskLevel riskLevel;
    @NotBlank
    public String requestedBy;
    @NotBlank
    public String payload;
}

package com.hlr.app.dto;

import jakarta.validation.constraints.NotBlank;

public class ApprovalRequest {
    @NotBlank
    public String approver;
    public boolean approved;
}

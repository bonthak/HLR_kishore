package com.hlr.app.dto;

import jakarta.validation.constraints.NotBlank;

public class NumberChangeRequest {
    @NotBlank
    public String oldMsisdn;
    @NotBlank
    public String newMsisdn;
    @NotBlank
    public String actor;
    public String reason = "MSISDN reassignment";
}

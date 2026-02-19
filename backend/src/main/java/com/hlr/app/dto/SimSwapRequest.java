package com.hlr.app.dto;

import jakarta.validation.constraints.NotBlank;

public class SimSwapRequest {
    @NotBlank
    public String oldImsi;
    @NotBlank
    public String newImsi;
    @NotBlank
    public String newIccid;
    @NotBlank
    public String actor;
    public String reason = "SIM swap";
}

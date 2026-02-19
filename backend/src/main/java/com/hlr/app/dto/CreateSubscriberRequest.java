package com.hlr.app.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSubscriberRequest {
    @NotBlank
    public String externalRef;
    @NotBlank
    public String market;
    @NotBlank
    public String imsi;
    @NotBlank
    public String msisdn;
    @NotBlank
    public String iccid;
    public boolean baoc;
    public boolean boic;
    public boolean baic;
    public boolean clip = true;
    public boolean clir;
    public boolean callWaiting = true;
    public boolean gprsEnabled = true;
    @NotBlank
    public String apnProfile;
    @NotBlank
    public String camelProfileVersion;
    public boolean roamingEnabled;
    public String allowedVplmnCsv = "";
    public String barredVplmnCsv = "";
    public String regionClass = "DEFAULT";
}

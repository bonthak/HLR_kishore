package com.hlr.app.dto;

import com.hlr.app.entity.SubscriberState;

import java.util.UUID;

public class SubscriberResponse {
    public UUID id;
    public String externalRef;
    public String market;
    public SubscriberState state;
    public String activeImsi;
    public String activeMsisdn;
    public String iccid;
    public boolean roamingEnabled;
}

CREATE TABLE subscribers (
    id UUID PRIMARY KEY,
    external_ref VARCHAR(100) NOT NULL UNIQUE,
    state VARCHAR(30) NOT NULL,
    market VARCHAR(40) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE identity_mappings (
    id UUID PRIMARY KEY,
    imsi VARCHAR(30) NOT NULL UNIQUE,
    msisdn VARCHAR(30) NOT NULL UNIQUE,
    iccid VARCHAR(30) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL,
    effective_from TIMESTAMPTZ NOT NULL,
    effective_to TIMESTAMPTZ,
    subscriber_id UUID NOT NULL REFERENCES subscribers(id)
);

CREATE TABLE service_profiles (
    id UUID PRIMARY KEY,
    baoc BOOLEAN NOT NULL,
    boic BOOLEAN NOT NULL,
    baic BOOLEAN NOT NULL,
    clip BOOLEAN NOT NULL,
    clir BOOLEAN NOT NULL,
    call_waiting BOOLEAN NOT NULL,
    gprs_enabled BOOLEAN NOT NULL,
    apn_profile VARCHAR(100) NOT NULL,
    camel_profile_version VARCHAR(40) NOT NULL,
    subscriber_id UUID NOT NULL UNIQUE REFERENCES subscribers(id)
);

CREATE TABLE roaming_policies (
    id UUID PRIMARY KEY,
    roaming_enabled BOOLEAN NOT NULL,
    allowed_vplmn_csv TEXT NOT NULL,
    barred_vplmn_csv TEXT NOT NULL,
    region_class VARCHAR(60) NOT NULL,
    effective_from DATE,
    effective_to DATE,
    subscriber_id UUID NOT NULL UNIQUE REFERENCES subscribers(id)
);

CREATE TABLE change_requests (
    id UUID PRIMARY KEY,
    operation_type VARCHAR(40) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    requested_by VARCHAR(80) NOT NULL,
    approved_by VARCHAR(80),
    payload TEXT NOT NULL,
    correlation_id VARCHAR(80) NOT NULL,
    requested_at TIMESTAMPTZ NOT NULL,
    approved_at TIMESTAMPTZ,
    subscriber_id UUID REFERENCES subscribers(id)
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    actor VARCHAR(80) NOT NULL,
    action VARCHAR(80) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(80) NOT NULL,
    before_state TEXT,
    after_state TEXT,
    correlation_id VARCHAR(80) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_identity_subscriber ON identity_mappings(subscriber_id);
CREATE INDEX idx_changereq_status ON change_requests(status);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);

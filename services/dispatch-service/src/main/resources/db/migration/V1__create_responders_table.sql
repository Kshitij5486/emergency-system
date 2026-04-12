CREATE TYPE dispatch.responder_type   AS ENUM ('AMBULANCE','POLICE','FIRE','RESCUE','HAZMAT');
CREATE TYPE dispatch.responder_status AS ENUM ('AVAILABLE','BUSY','OFF_DUTY','MAINTENANCE');
CREATE TABLE dispatch.responders (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(255)              NOT NULL,
    type                dispatch.responder_type   NOT NULL,
    status              dispatch.responder_status NOT NULL DEFAULT 'AVAILABLE',
    phone_number        VARCHAR(20)               NOT NULL,
    vehicle_id          VARCHAR(50),
    latitude            DOUBLE PRECISION          NOT NULL,
    longitude           DOUBLE PRECISION          NOT NULL,
    city                VARCHAR(100)              NOT NULL DEFAULT 'default',
    current_incident_id UUID,
    last_ping_at        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ               NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ               NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_responders_status      ON dispatch.responders (status);
CREATE INDEX idx_responders_city_status ON dispatch.responders (city, status);
CREATE INDEX idx_responders_available   ON dispatch.responders (city, type, latitude, longitude)
    WHERE status = 'AVAILABLE';

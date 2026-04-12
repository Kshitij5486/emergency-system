CREATE TYPE incidents.incident_type AS ENUM ('MEDICAL','FIRE','ACCIDENT','CRIME','NATURAL_DISASTER','OTHER');
CREATE TYPE incidents.incident_status AS ENUM ('REPORTED','QUEUED','DISPATCHED','IN_PROGRESS','RESOLVED','CANCELLED','FAKE');
CREATE TABLE incidents.incidents (
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type                  incidents.incident_type   NOT NULL,
    status                incidents.incident_status NOT NULL DEFAULT 'REPORTED',
    severity              INTEGER          NOT NULL CHECK (severity BETWEEN 1 AND 5),
    description           TEXT,
    reporter_id           UUID             NOT NULL,
    assigned_responder_id UUID,
    latitude              DOUBLE PRECISION NOT NULL CHECK (latitude  BETWEEN -90  AND 90),
    longitude             DOUBLE PRECISION NOT NULL CHECK (longitude BETWEEN -180 AND 180),
    address               VARCHAR(500),
    city                  VARCHAR(100)     NOT NULL DEFAULT 'default',
    priority_score        SMALLINT         CHECK (priority_score BETWEEN 1 AND 5),
    fake_probability      DOUBLE PRECISION CHECK (fake_probability BETWEEN 0 AND 1),
    reported_at           TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    dispatched_at         TIMESTAMPTZ,
    resolved_at           TIMESTAMPTZ,
    updated_at            TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_incidents_status      ON incidents.incidents (status);
CREATE INDEX idx_incidents_reporter    ON incidents.incidents (reporter_id);
CREATE INDEX idx_incidents_city_status ON incidents.incidents (city, status);
CREATE INDEX idx_incidents_active      ON incidents.incidents (status, priority_score DESC, reported_at)
    WHERE status NOT IN ('RESOLVED','CANCELLED','FAKE');

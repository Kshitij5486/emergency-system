CREATE TABLE incidents.incident_status_history (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    incident_id UUID NOT NULL REFERENCES incidents.incidents(id) ON DELETE CASCADE,
    from_status incidents.incident_status,
    to_status   incidents.incident_status NOT NULL,
    changed_by  UUID NOT NULL,
    changed_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    note        VARCHAR(500)
);
CREATE INDEX idx_status_history_incident ON incidents.incident_status_history (incident_id, changed_at DESC);

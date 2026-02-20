-- RequestLog & ResponseLog tabloları (BiletBankDB)

CREATE TABLE IF NOT EXISTS request_log (
    id              BIGSERIAL PRIMARY KEY,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    method          VARCHAR(10),
    uri             VARCHAR(2048),
    query_string    VARCHAR(2048),
    client_address  VARCHAR(64),
    headers         TEXT,
    body            TEXT
);

CREATE TABLE IF NOT EXISTS response_log (
    id              BIGSERIAL PRIMARY KEY,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    request_log_id  BIGINT REFERENCES request_log(id),
    status_code     INTEGER,
    headers         TEXT,
    body            TEXT,
    duration_ms     BIGINT
);

CREATE INDEX IF NOT EXISTS idx_request_log_created_at ON request_log(created_at);
CREATE INDEX IF NOT EXISTS idx_response_log_request_log_id ON response_log(request_log_id);
CREATE INDEX IF NOT EXISTS idx_response_log_created_at ON response_log(created_at);

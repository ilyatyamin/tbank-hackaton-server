CREATE TABLE IF NOT EXISTS requests (
    request_id bigserial PRIMARY KEY,
    request_time timestamp with time zone NOT NULL,
    brand_id text NOT NULL,
    offer_begin_date timestamp with time zone NOT NULL,
    cashback_percent
    status CalcStatus NOT NULL DEFAULT 'FAILED',
    CONSTRAINT correct_rt CHECK (request_time >= CURRENT_TIMESTAMP), -- for train only
    CONSTRAINT correct_obd CHECK (offer_begin_date >= CURRENT_TIMESTAMP) -- for train only
);
DROP TABLE answers;
CREATE TABLE IF NOT EXISTS answers (
    request_id bigint PRIMARY KEY,
    gross_merchandise_value double precision NOT NULL,
    purchase_count bigint NOT NULL,
    total_cashback double precision NOT NULL,
    CONSTRAINT fk_request_id FOREIGN KEY(request_id) REFERENCES requests(request_id) ON DELETE CASCADE,
    CONSTRAINT correct_gmv CHECK (gross_merchandise_value > 0), -- for train only
    CONSTRAINT correct_purchase_count CHECK (purchase_count > 0), -- for train only
    CONSTRAINT correct_cashback CHECK (total_cashback > 0) -- for train only
);

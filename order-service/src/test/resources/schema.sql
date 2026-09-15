-- Day 4: minimal schema for the H2 test profile (the jsonb outbox column is a
-- plain VARCHAR here; production runs on PostgreSQL via ddl-auto: update).
-- Domain: Order aggregate + OrderItem VO collection
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    order_id UUID NOT NULL,
    line_no INT NOT NULL,
    sku VARCHAR(64) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS outbox_messages (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    type VARCHAR(64) NOT NULL,
    payload VARCHAR(4000) NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

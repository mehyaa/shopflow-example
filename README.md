# ShopFlow

> **Example / reference project** from a Spring Boot microservices training course — not production software.

ShopFlow is an end-to-end e-commerce microservices platform built with Java 21, Spring Boot 4.1, and Spring Cloud 2025.1.3: eight services, an orchestrated saga with compensation, a transactional outbox, JWT security via Keycloak, and a full OpenTelemetry observability pipeline.

The architectural contract (services, ports, REST API, events, saga flow) lives in [**SPEC.md**](SPEC.md) — it is the single source of truth for the codebase.

## Tech Stack

Java 21 · Spring Boot 4.1.1 · Spring Cloud 2025.1.3 · PostgreSQL 18 · RabbitMQ 4.3 · Keycloak 26.7 · Docker · Kubernetes (Minikube)

## Modules

```mermaid
---
config:
  treeView:
    showIcons: true
---
treeView-beta
  shopflow/
    common-events/         # Shared event records (contract: SPEC §4)
    discovery-server/      # Eureka (8761)
    config-server/         # Spring Cloud Config - native (8888)
    api-gateway/           # Spring Cloud Gateway + JWT (8080)
    product-service/       # Product catalog (8081, Postgres)
    inventory-service/     # Stock reservation (8082, Postgres)
    order-service/         # Orders + saga orchestrator + outbox + circuit breaker (8083)
    payment-service/       # Payment stub, ~20% FAILED on purpose (8084)
    notification-service/  # RabbitMQ consumer (8085, H2)
```

## Quick Start (full stack)

```bash
./mvnw clean package -DskipTests
docker compose up -d --build
# Gateway:      http://localhost:8080
# Eureka:       http://localhost:8761
# RabbitMQ UI:  http://localhost:15672 (guest/guest)
# Keycloak:     http://localhost:8180  (admin/admin)
# Zipkin UI:    http://localhost:9411
# Metrics:      curl -s localhost:8889/metrics | grep -i http | head
```

### Optional ELK profile (log collection)

```bash
docker compose --profile elk up -d --build
```

The ELK stack (Elasticsearch + Logstash + Kibana + logspout) collects all container
logs. **Kibana:** http://localhost:5601 → create index pattern `shopflow-logs-*`
in Discover.

> **RAM warning:** the ELK stack adds ~3 GB on top of the base stack. On
> low-resource machines, skip this profile; Zipkin + the metrics pipeline are enough.

End-to-end order (requires a token once security is enabled):

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"customerId":"11111111-1111-1111-1111-111111111111","items":[{"sku":"TSHIRT-001","quantity":2}]}'
```

## Running Locally (without compose)

```bash
# 1) infrastructure
docker run -d --name shopflow-pg -e POSTGRES_USER=shopflow -e POSTGRES_PASSWORD=shopflow \
  -e POSTGRES_MULTIPLE_DATABASES... (see docker/postgres/init.sql) -p 5432:5432 postgres:18-alpine
docker run -d --name shopflow-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:4-management

# 2) platform
./mvnw spring-boot:run -pl discovery-server
./mvnw spring-boot:run -pl config-server

# 3) business services
./mvnw spring-boot:run -pl product-service
./mvnw spring-boot:run -pl inventory-service
./mvnw spring-boot:run -pl order-service
./mvnw spring-boot:run -pl payment-service
./mvnw spring-boot:run -pl notification-service

# 4) gateway
./mvnw spring-boot:run -pl api-gateway
```

## Observability (OTel)

Services started via compose are auto-instrumented with the **OpenTelemetry Java agent**
(2.31.1): agent → OTLP → `otel-collector` (0.160.0) → Zipkin (3). Architecture and
contract: [SPEC.md §11](SPEC.md#11-observability--otel-pipeline).

- Nothing extra needed in compose — `zipkin` and `otel-collector` start automatically;
  business services + gateway run with the agent (discovery/config without it).
- Zipkin UI: http://localhost:9411
- Metrics: the agent sends OTLP metrics; the collector serves them in Prometheus
  format on `:8889` → `curl -s localhost:8889/metrics | grep -i http | head`
- Logs: optional `--profile elk` streams JSON logs into Kibana (section above).
- Verification: place an order (curl above), open Zipkin → "Run Query" →
  inspect the `POST /api/orders` trace; you should see the span tree.

When running without compose, start the agent explicitly:

```bash
java -javaagent:docker/otel/opentelemetry-javaagent.jar \
  -Dotel.service.name=order-service \
  -Dotel.exporter.otlp.endpoint=http://localhost:9411/api/v2/spans \
  -jar target/order-service-1.0.0.jar
# or: ./scripts/run-with-otel.sh order-service
```

## Minikube (Kubernetes)

```bash
minikube start --memory=6g --cpus=2
./mvnw clean package -DskipTests
for s in common-events discovery-server config-server api-gateway product-service inventory-service order-service payment-service notification-service; do
  minikube image load shopflow/$s:1.0.0   # images must have been built by compose build
done
kubectl apply -f k8s/
kubectl get pods -n shopflow -w
# Gateway NodePort: http://$(minikube ip):30080
```

## Tests

```bash
./mvnw test          # one context-load test per service (test profile: H2)
```

## License

[MIT](LICENSE)

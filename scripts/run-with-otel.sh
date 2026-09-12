#!/usr/bin/env bash
# Run a ShopFlow module locally with the OpenTelemetry Java agent attached.
#
# Usage:
#   ./scripts/run-with-otel.sh <module-name>       e.g. ./scripts/run-with-otel.sh order-service
#
# Traces are sent to the OTLP endpoint in OTEL_EXPORTER_OTLP_ENDPOINT
# (default: local Zipkin directly — start it with:
#   docker run -d --name shopflow-zipkin -p 9411:9411 openzipkin/zipkin:3
# ). Zipkin UI: http://localhost:9411
set -euo pipefail

cd "$(dirname "$0")/.."

MODULE="${1:?Usage: ./scripts/run-with-otel.sh <module-name>}"

AGENT_JAR="docker/otel/opentelemetry-javaagent.jar"

# Download the agent if missing (same version as docker-compose uses)
if [ ! -f "$AGENT_JAR" ]; then
  echo "OTel agent not found, downloading..."
  mkdir -p "$(dirname "$AGENT_JAR")"
  curl -fSL -o "$AGENT_JAR" \
    "https://repo1.maven.org/maven2/io/opentelemetry/javaagent/opentelemetry-javaagent/2.10.0/opentelemetry-javaagent-2.10.0.jar"
fi

# Default: export straight to local Zipkin (no collector needed for a quick demo).
export OTEL_SERVICE_NAME="${OTEL_SERVICE_NAME:-$MODULE}"
export OTEL_EXPORTER_OTLP_ENDPOINT="${OTEL_EXPORTER_OTLP_ENDPOINT:-http://localhost:9411/api/v2/spans}"
export OTEL_EXPORTER_OTLP_PROTOCOL="http/protobuf"
export OTEL_TRACES_EXPORTER="otlp"
export OTEL_METRICS_EXPORTER="none"
export OTEL_LOGS_EXPORTER="none"
export JAVA_TOOL_OPTIONS="-javaagent:${AGENT_JAR}"

echo ">>> Starting '$MODULE' with OTel agent (service=$OTEL_SERVICE_NAME, endpoint=$OTEL_EXPORTER_OTLP_ENDPOINT)"
./mvnw spring-boot:run -pl "$MODULE"

#!/bin/bash
# Creates all Kafka topics for the emergency system
# Run once after Kafka is healthy

KAFKA=kafka:29092

echo "Creating emergency-system Kafka topics..."

kafka-topics --create --if-not-exists \
  --bootstrap-server $KAFKA \
  --topic emergency-events \
  --partitions 6 \
  --replication-factor 1

kafka-topics --create --if-not-exists \
  --bootstrap-server $KAFKA \
  --topic dispatch-updates \
  --partitions 3 \
  --replication-factor 1

kafka-topics --create --if-not-exists \
  --bootstrap-server $KAFKA \
  --topic notifications \
  --partitions 3 \
  --replication-factor 1

kafka-topics --create --if-not-exists \
  --bootstrap-server $KAFKA \
  --topic emergency-events-dlt \
  --partitions 3 \
  --replication-factor 1

kafka-topics --create --if-not-exists \
  --bootstrap-server $KAFKA \
  --topic dispatch-updates-dlt \
  --partitions 3 \
  --replication-factor 1

echo "All topics created. Listing:"
kafka-topics --list --bootstrap-server $KAFKA
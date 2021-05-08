#!/bin/sh

# Primero compilamos el codigo y generamos un jar
mvn -U clean install -DskipTests
docker-compose -p "mikkezavalacd " up --build
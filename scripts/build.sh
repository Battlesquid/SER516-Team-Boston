#!/bin/bash

docker-compose build
mvn -pl client -am -DskipTests clean install

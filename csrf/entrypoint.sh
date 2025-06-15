#!/bin/bash

# code-server 백그라운드 실행
nohup code-server --auth password --port 8081 &

# Spring Boot 서버 실행 (devtools 적용)
cd /home/coder/project
./gradlew bootRun

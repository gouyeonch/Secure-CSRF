#!/bin/bash
# 백그라운드로 code-server 실행
nohup code-server --auth password --port 8081 --bind-addr 0.0.0.0:8081 &

# Spring devtools 자동 반영 가능하도록 실행
cd /home/coder/project
./gradlew bootRun

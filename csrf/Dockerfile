FROM codercom/code-server:latest

USER root
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk curl unzip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 코드 복사
COPY . /home/coder/project

# entrypoint 스크립트 복사 및 실행권한
COPY entrypoint.sh /home/coder/entrypoint.sh
RUN chmod +x /home/coder/entrypoint.sh

USER coder
WORKDIR /home/coder/project

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT ["/home/coder/entrypoint.sh"]

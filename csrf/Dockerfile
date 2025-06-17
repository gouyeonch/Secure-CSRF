FROM codercom/code-server:latest

USER root

# 필수 패키지 설치 (OpenJDK + nginx)
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk curl unzip nginx && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 프로젝트 및 공격자 페이지 복사
COPY . /home/coder/project
COPY attacker/attacker.html /var/www/html/index.html

# nginx 기본 포트 8082로 변경
RUN sed -i 's/80 default_server/8082 default_server/' /etc/nginx/sites-enabled/default

# 권한 설정
RUN chown -R coder:coder /home/coder/project

# 워크디렉토리 및 entrypoint 설정
WORKDIR /home/coder/project
COPY entrypoint.sh /home/coder/entrypoint.sh
RUN chmod +x /home/coder/entrypoint.sh

USER coder
EXPOSE 8080 8081 8082
ENTRYPOINT ["/home/coder/entrypoint.sh"]

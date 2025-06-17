FROM codercom/code-server:latest

USER root
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk curl unzip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY . /home/coder/project
RUN chown -R coder:coder /home/coder/project

WORKDIR /home/coder/project
COPY entrypoint.sh /home/coder/entrypoint.sh
RUN chmod +x /home/coder/entrypoint.sh

USER coder
EXPOSE 8080 8081
ENTRYPOINT ["/home/coder/entrypoint.sh"]

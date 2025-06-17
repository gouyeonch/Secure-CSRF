# 🛡️ CSRF 보안 실습 통합 환경

이 프로젝트는 CSRF(Cross Site Request Forgery) 보안 실습을 위한 **Spring 서버**와 **공격자 서버(HTML 폼)**, 그리고 **브라우저 기반 코드 에디터(code-server)**를 하나의 Docker Compose 환경으로 구성합니다.

## 📦 구성 서비스

| 서비스         | 포트         | 설명                                  |
|----------------|--------------|---------------------------------------|
| Spring 서버     | `8080`       | 게시판 기능 제공 (CSRF 취약점 포함)   |
| code-server     | `8081`       | VSCode 웹 에디터 (비밀번호: `mypassword`) |
| 공격자 페이지   | `8082`       | CSRF 공격 HTML (`attacker.html`)      |

---

## 🚀 실행 방법

### 1. Docker & Docker Compose 설치

- Docker: https://docs.docker.com/get-docker/
- Docker Compose: Docker Desktop에 기본 포함됨

```bash
docker --version
docker compose version
```

### 2. docker-compose.yml만 있으면 OK

```bash
# docker-compose.yml 파일이 있는 폴더에서 실행
docker compose up -d
```
-d: 백그라운드 실행

### 3. 서비스 접속
- http://localhost:8080 → 게시판 (Spring 서버)

- http://localhost:8081 → 코드 수정 (code-server, 비번: mypassword)

- http://localhost:8082/attacker.html → 공격자 페이지

## 🧪 실습 예시
### ✅ 정상 게시글 작성
1. http://localhost:8080/login-form → 로그인

2. http://localhost:8080/new.html → 게시글 작성

### ❌ CSRF 공격 (방지됨)
1. http://localhost:8082/attacker.html 접속

2. SameSite=Strict + CSRF Token 검증으로 공격 차단됨

## 🔁 코드 수정 & 반영
방법 1: code-server에서 코드 수정
수정 후 자동 반영되면 OK

자동 반영이 안 되면 아래처럼 컨테이너만 재시작:
```bash
docker restart spring-server
```

## 🐳 Docker Hub 이미지
직접 빌드하지 않고, 아래 공개 이미지를 사용할 수 있습니다.

spring-server	: gouyeonch/spring-server:latest<br>
attacker-server	: gouyeonch/attacker-server:latest









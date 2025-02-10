# 식당 예약 API 🍽️

이 프로젝트는 레스토랑 예약을 관리하는 시스템입니다.  
고객이 온라인에서 예약을 진행하고, 관리자는 예약 및 결제를 처리할 수 있습니다.

## 주요 기능
- 고객 예약 생성 및 관리
- 관리자 승인 및 예약 상태 변경
- 결제 시스템 (MySQL + Spring Security)

## 🛠️ 기술 스택

- **Backend**: Java, Spring Boot , Spring Security, JPA
- **Database**: MySQL 
- **DevOps**: Docker, Docker Compose, CI/CD (GitHub Actions)- 예정중
- **API Documentation**: Swagger (SpringDoc)

## 🚀 프로젝트 실행 방법

### 1. 환경 변수 설정
`src/main/resources/application.yml` 또는 `.env` 파일을 생성하여 DB 정보를 입력하세요.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurant_reservation
    username: zini_reservation_user
    password: kukugi251001
==

테이블 관계
user: 
payment:  reseravtion 이랑 ManyToOne
reservation: user,restaurant table 이랑 ManyToOne
restaurant table: 

엔드포인트


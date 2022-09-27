# 빌리마켓 (중고 물품 대여 서비스)
사용자간 중고 물품을 대여할 수 있는 웹 서비스 플랫폼입니다.

### 진행기간
2022/03/07 ~ 2022/06/07

### 팀 구성원
PM 1명, 아키텍처 1명, 프론트엔드 3명, 백엔드 3명, QA 1명

### 사용 기술
Spring Boot-2.6.4, Gradle, Jpa, AWS RDS(MariaDB), Redis-3.0.5, ElasticSearch-7.15.2, LogStash-7.15.2

### 아키텍처


### 데이터 모델


### 참여한 내용
- ElasticSearch와 한글 형태소 분석기 Nori를 활용한 물품 검색 기능 구현
- Spring Security + Jwt를 활용한 로그인 및 자원 접근 권한 처리
- WebSocket + Stomp를 활용한 사용자간 채팅 기능 구현
- AWS S3 파일 처리와 EventListener를 이용한 채팅 내역 저장 기능 구현
- 금융 결제원 API를 활용한 결제 시스템 구현
- MariaDB의 ST_Distance_Sphere 함수와 QueryDSL을 이용한 주변 물품 탐색 기능 구현
- ControllerAdvice를 활용한 Exception Handling 처리
- Swagger API를 사용한 API 문서 생성

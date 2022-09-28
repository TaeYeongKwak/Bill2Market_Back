# 빌리마켓 (중고 물품 대여 서비스)
사용자간 중고 물품을 대여할 수 있는 웹 서비스 플랫폼입니다.

### 진행기간
2022/03/07 ~ 2022/06/07

### 팀 구성원
PM 1명, 아키텍처 1명, 프론트엔드 3명, 백엔드 3명, QA 1명

### 사용 기술
Spring Boot-2.6.4, Gradle, Jpa, AWS RDS(MariaDB), Redis-3.0.5, ElasticSearch-7.15.2, LogStash-7.15.2

### 아키텍처
![bill2_architecture](https://user-images.githubusercontent.com/75138553/192664171-73d17c9c-0824-4d85-aadb-d154ff957e19.PNG)


### 데이터 모델
![bill2_datamodel](https://user-images.githubusercontent.com/75138553/192664041-f0e92b80-2e06-41ba-b2ba-a40e25c3e93d.jpg)

### 참여한 내용
- ElasticSearch와 한글 형태소 분석기 Nori를 활용한 물품 검색 기능 구현
- Spring Security + Jwt를 활용한 로그인 및 자원 접근 권한 처리
- WebSocket + Stomp + Redis를 활용한 사용자간 채팅 기능 구현
- AWS S3 파일 처리와 EventListener를 이용한 채팅 내역 저장 기능 구현
- 금융 결제원 API를 활용한 결제 시스템 구현
- MariaDB의 ST_Distance_Sphere 함수와 QueryDSL을 이용한 주변 물품 탐색 기능 구현
- ControllerAdvice를 활용한 Exception Handling 처리
- Swagger API를 사용한 API 문서 생성

### 발표자료
[빌리마켓_발표자료.pdf](https://github.com/TaeYeongKwak/Bill2Market_Back/files/9660531/default.pdf)

# 목차
1. 서비스 소개 및 바로가기
2. 사용 기술
3. ERD
4. API Docs
5. InfraStructure
6. Main Function
7. 기술적인 고민과 개선 내용

<br>

## 1.  [<img width="40" height="40" align="absmiddle" src="https://github.com/user-attachments/assets/b96af13e-bf85-481b-bad7-946c55480740" /> ssup 바로가기](https://ssup.site/posts)

ssup은 한국에 거주하는 외국인과, 외국어에 관심이 있거나 공부하고 있는 한국인을 연결시켜주는 소셜 앱입니다.

기존에 존재하는 유료 화상영어/오프라인 모임과 달리, 비용을 들이지 않고 가까이 있는 언어 교류 친구를 만들 수 있는 서비스를 제공합니다.

![2026-01-0512 32 37-ezgif com-video-to-gif-converter (1)](https://github.com/user-attachments/assets/526b81a7-5271-4c9d-b2e5-fb984411daac)


<br>


## 2. 사용 기술
### Frontend
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)

### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

### Infra, CI/CD
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![CloudFront](https://img.shields.io/badge/CloudFront-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=orange)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)


<br>

## 3. [ERD](https://dbdiagram.io/d/ssup-6930e8afd6676488ba811397)

https://dbdiagram.io/d/ssup-6930e8afd6676488ba811397
<img width="900" height="609" alt="스크린샷 2026-01-05 오전 10 06 25" src="https://github.com/user-attachments/assets/039218c2-e503-4a5a-a2c6-76e93371978a" />

<br>

## 4. [API Docs  ![Swagger](https://img.shields.io/badge/-Swagger-%2385EA2D?style=flat-square&logo=swagger&logoColor=black)](https://api.ssup.site/swagger-ui/index.html)
https://api.ssup.site/swagger-ui/index.html

<img width="700" height="600" alt="스크린샷 2026-01-05 오전 11 49 03" src="https://github.com/user-attachments/assets/151e05dd-9008-4f85-8dce-ffde4bc25fa2" />

<br>

## 7. 고민한 부분들
[매칭 시스템 도메인 설계 및 조회]

**문제**
 - 매치 시스템에서, 유저(User)는 '신청자' 또는 '수신자'이다.
 - 유저의 매치 기록이 요구사항에 포함되어 있었기 때문에 양방향 관계(user.getMatches()를 통해 조회)를 고려.
 - 하지만 User-Match-User의 관계는 순환참조, 도메인 간 복잡한 결합이 생길 위험이 있다.
 - User 조회 시, 유저의 매치 내역까지 영속성 컨텍스트에 불필요하게 로드되기 때문에, LazyInitializationException와 N+1 리스크가 생김.

**해결**
  - Match와 User 엔티티를 단방향 관계로 구현.
  - JPQL을 통해 연관관계의 주인인 Match를 통해 매치 내역을 포함한 모든 작업을 수행하도록 구현.

**결과**
  - 복잡한 양방향 매핑을 지양하고, 도메인 간 결합을 낮추면서도 요구사항을 충족

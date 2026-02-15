## 목차
1. 서비스 소개 및 바로가기
2. 사용 기술
3. 기술적인 고민과 개선 내용
4. ERD
5. API Docs
6. 서버 아키텍처
7. 주요 기능 시연 영상

<br>

## 1.  [<img width="40" height="40" align="absmiddle" src="https://github.com/user-attachments/assets/b96af13e-bf85-481b-bad7-946c55480740" /> ssup 바로가기](https://ssup.site/posts)

ssup은 한국에 거주하는 외국인과, 외국어에 관심있는 한국인을 연결시켜주는 언어교류 소셜 앱입니다.

기존의 유료 화상영어/오프라인 모임과 달리, 비용을 들이지 않고 가까이 있는 언어 교류 친구를 만들 수 있는 서비스를 제공합니다.


<br>


## 2. 사용 기술
### Frontend
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white) 
![React Router](https://img.shields.io/badge/React%20Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![H2](https://img.shields.io/badge/H2-00447C?style=for-the-badge&logo=databricks&logoColor=white)

### Infra, CI/CD
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![CloudFront](https://img.shields.io/badge/CloudFront-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=orange)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)


<br>



## 3. 기술적 고민과 해결

### [매칭 시스템 도메인 설계 및 조회]

**문제**
 - 매치 시스템에서, 유저(User)는 '신청자' 또는 '수신자'이다
 - 유저의 매치 기록이 요구사항에 포함되어 있었기 때문에 양방향 관계(user.getMatches()를 통해 조회)를 고려
 - 하지만 User-Match-User의 관계는 순환참조, 도메인 간 복잡한 결합이 생길 위험이 있다
 - User 조회 시, 유저의 매치 내역까지 영속성 컨텍스트에 불필요하게 로드되기 때문에, LazyInitializationException와 N+1 리스크가 존재

**해결**
  - Match와 User 엔티티를 단방향 관계로 구현
  - JPQL을 통해 연관관계의 주인인 Match를 통해 매치 내역을 포함한 모든 작업을 수행하도록 구현

**결과**
  - 복잡한 양방향 매핑과 도메인 간 결합 방지


### [DB Index 생성을 통한 Full Table Scan 방지 및 5배 성능 개선]

**문제**
 - 매치 이력 조회 시, 여러개의 OR 조건이 포함된 쿼리가 존재
 - 이 경우, 복잡한 조건을 처리하기 위해 인덱스를 여러번 갈아타야 하는데, 이때 DB Optimizer가 Full Table Scan의 비용이 더 낮다고 판단할때가 많음. (실제 DB에서 `type: ALL` 확인)
 - 따라서 복합 인덱스가 필요
 
**해결**
  - 조회에 필요한 필드들을 포함한 복합 인덱스를 구성
  - ```sql
    CREATE INDEX idx_match_requester_status_receiver ON matches (requester_id, receiver_id, status);
    ```
  - index 외에도 `union all` 사용, 두 개의 조회 쿼리로 분리 등 다양한 대안에 대해 학습

**결과**
  - dummy data 10만건으로 테스트하여, 조회 성능 5배 개선을 확인
  - 기존 104999개의 row를 탐색 -> 개선 후 6개의 row만 탐색


### [TDD 기반 백엔드 개발 및 테스트 커버리지 80% 유지]

**계기**
 - 코드 안정성과 시스템 신뢰도를 위해 TDD 기반 백엔드 개발 진행
 - PR 단계에 CI를 적용함으로써 branch/배포환경 안정성 보장

**결과**
  - 테스트 커버리지 80% 유지 (Jacoco 기반 테스트)
  - 운영 환경에서의 예측 불가능한 오류 방지


### [성능과 확장성을 고려한 데이터 필터링 설계]

**계기**
 - 게시글 다중 조건 필터링 구현 시, 다음과 같은 문제가 존재
 - frontend   
   1. 필터링 UI에 필요한 metaData를 불러오기 위해, 필터링 요청마다 3개의 api를 호출해야함(/locations, /languages, /interests)
   2. 이 api들을 매번 호출하게 되면 성능에 치명적일 수 있다.

 - backend
   1. 다중 조건 필터링을 구현하려면 where문에 복잡한 조건을 나열해야하는데, 향후 필터 조건이 늘어날수록 쿼리가 길어져 가독성이 저하되고 메서드 수가 증가하게 됨
   2. 쿼리가 길어질수록 휴먼 에러 발생 가능성이 증가하고, 필터 조건을 선택하지 않았을 경우에 대한 별도의 처리(null 처리)가 필요

**해결**
 - 필터 metaData를 LocalStorage에 캐싱하고, 필터 조회 시마다 cached 데이터를 사용
 - Querydsl을 도입하여 각 필터 조건을 메서드화
 - 조회수 기반 정렬 시 발생하는 중복 스코어 문제를 id 비교 연산(lt)을 통해 기존의 데이터 누락이 없는 무한 스크롤 기능 유지

**결과**
 - 변경이 거의 없는 metaData 캐싱을 통해 시스템 부하 방지
 - Querydsl을 통한 쿼리 유지보수 및 가독성 증진, null 처리 간편화
 - 길이가 긴 다중 조건 쿼리 작성 시 발생할 수 있는 휴먼 에러 방지

<br>


## 4. [ERD](https://dbdiagram.io/d/ssup-6930e8afd6676488ba811397)

https://dbdiagram.io/d/ssup-6930e8afd6676488ba811397
<img width="900" height="609" alt="스크린샷 2026-01-05 오전 10 06 25" src="https://github.com/user-attachments/assets/039218c2-e503-4a5a-a2c6-76e93371978a" />

<br>

## 5. [API Docs  ![Swagger](https://img.shields.io/badge/-Swagger-%2385EA2D?style=flat-square&logo=swagger&logoColor=black)](https://api.ssup.site/swagger-ui/index.html)
https://api.ssup.site/swagger-ui/index.html

<img width="700" height="600" alt="스크린샷 2026-01-05 오전 11 49 03" src="https://github.com/user-attachments/assets/151e05dd-9008-4f85-8dce-ffde4bc25fa2" />

<br>

## 6. 서버 아키텍처(Server Architecture) 및 CI/CD Pipeline

### 서버 아키텍처
<img width="1222" height="736" alt="ssup_infra_image_2" src="https://github.com/user-attachments/assets/6f28852b-9a72-4ec4-83d6-3f3db2d0f671" />


### CI/CD 구조
<img width="1289" height="554" alt="ssup_ci:cd-image" src="https://github.com/user-attachments/assets/6f7a5acc-ff2a-47bf-938a-1f49f6fda344" />

<br>


## 7. 주요 기능 (Main Function)

### 이메일 로그인
![ssup_로그인_gif](https://github.com/user-attachments/assets/aaaea172-3187-48a9-8755-991231ff3b78)

### 댓글 작성
![ssup_댓글작성_gif](https://github.com/user-attachments/assets/13f1cecc-2e43-47f2-8dd4-3c077dae5a11)

### 매치 요청
![ssup_매치요청_gif](https://github.com/user-attachments/assets/8a62e9c6-999d-425d-ba60-6676a5698057)

### 매치 수락
![ssup_매치수락_gif](https://github.com/user-attachments/assets/889635de-f36c-4249-97fb-bd824208cda3)






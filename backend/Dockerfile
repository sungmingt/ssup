# 1. Build stage - jar 빌드
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle 캐시를 위해 gradle 파일 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# gradle wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 소스 복사
COPY src ./src

# JAR 빌드
RUN ./gradlew clean build -x test

# 2. Run stage - 앱 실행
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드한 jar 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

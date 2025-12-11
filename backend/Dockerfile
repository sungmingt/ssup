# 1. JDK가 포함된 공식 이미지 사용
FROM eclipse-temurin:17-jdk

# 2. JAR 파일을 이미지 안으로 복사
COPY build/libs/*.jar app.jar

# 3. 앱 실행
ENTRYPOINT ["java","-jar","/app.jar"]
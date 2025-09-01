FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle 래퍼 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# Gradle 실행 권한 부여
RUN chmod +x ./gradlew

# 테스트 제외하고 빌드 - 빌드 성능 최적화
RUN ./gradlew build -x test --no-daemon --parallel --build-cache

# 런타임 이미지
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 포트 노출
EXPOSE 8081
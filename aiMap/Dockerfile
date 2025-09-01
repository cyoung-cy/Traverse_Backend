# 1단계: 빌드용 (build stage)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle 설치 - 캐시 레이어 최적화
RUN apt-get update && apt-get install -y curl unzip zip && \
    curl -O https://downloads.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip gradle-8.5-bin.zip && \
    mv gradle-8.5 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
    rm gradle-8.5-bin.zip

# 소스 코드 복사
COPY . .

# Gradle Wrapper 생성 및 빌드 - 빌드 성능 최적화
RUN gradle wrapper --gradle-version 8.5
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon --parallel --build-cache -x test

# 2단계: 실행용 (runtime stage)
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 오픈
EXPOSE 8081

# 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]

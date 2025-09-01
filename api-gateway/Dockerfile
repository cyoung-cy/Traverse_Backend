FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Gradle 설치
RUN apt-get update && \
    apt-get install -y curl unzip && \
    curl -O https://downloads.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip gradle-8.5-bin.zip && \
    mv gradle-8.5 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
    rm gradle-8.5-bin.zip

# Gradle 프로젝트 파일 복사
COPY build.gradle .
COPY settings.gradle .

# Gradle Wrapper 생성
RUN gradle wrapper --gradle-version 8.5 --distribution-type bin

# Gradle 의존성 다운로드 (캐시 레이어)
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon --parallel --build-cache

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test --no-daemon --parallel --build-cache --max-workers=4

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 
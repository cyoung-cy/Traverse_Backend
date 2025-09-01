# Traverse Backend 마이크로서비스

## 시스템 구조

TapTalk Backend는 다음 마이크로서비스로 구성되어 있습니다:

- **Eureka Server**: 서비스 디스커버리
- **API Gateway**: 모든 요청의 진입점, 라우팅 및 부하 분산
- **AI Map Service**: 지도 및 AI 관련 기능
- **User Auth Service**: 사용자 인증 및 관리
- **MariaDB**: 모든 서비스의 데이터 저장소

CI/CD : Git Actions

Server : 라즈베리파이4 8GB (자체 서버) - ubuntu

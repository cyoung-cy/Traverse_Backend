# TapTalk 시스템 API 명세서

## 개요
TapTalk 시스템은 마이크로서비스 아키텍처로 구성된 채팅 및 소셜 플랫폼입니다.

### 시스템 구성
- **API Gateway**: 모든 요청의 진입점 (포트: 8080)
- **User Auth Service**: 사용자 인증 및 관리자 기능 (포트: 8081)
- **AI Map Service**: AI 기반 퀘스트 및 추천 시스템 (포트: 8082)
- **Message Service**: 메시지 처리 서비스 (포트: 8083)
- **Notification Service**: 알림 서비스 (포트: 8084)
- **Eureka Server**: 서비스 디스커버리 (포트: 8761)

### Base URL
모든 API 요청은 API Gateway를 통해 이루어집니다:
```
http://211.187.162.65:8080
```

---

## 1. 인증 서비스 (User Auth Service)

### 1.1 인증 관련 API

#### 🔐 관리자 회원가입
```http
POST /api/auth/register
```

**Request Body:**
```json
{
  "name": "관리자명",
  "email": "admin@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

**Response:**
```json
{
  "success": true,
  "message": "회원가입 성공"
}
```

#### 🔑 관리자 로그인
```http
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "admin": {
      "id": 1,
      "name": "관리자명",
      "email": "admin@example.com",
      "role": "ADMIN"
    }
  }
}
```

#### 👥 관리자 목록 조회
```http
GET /api/auth/admins
```

**Response:**
```json
{
  "admins": [
    {
      "id": 1,
      "name": "관리자명",
      "role": "ADMIN"
    }
  ],
  "total_count": 1
}
```

#### 👤 관리자 상세 정보 조회
```http
GET /api/auth/admins/{adminId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "관리자명",
    "email": "admin@example.com",
    "role": "ADMIN",
    "permissions": ["VIEW_DASHBOARD", "MANAGE_USERS"],
    "status": "ACTIVE",
    "last_login_at": "2024-01-01T10:00:00Z",
    "created_at": "2024-01-01T09:00:00Z",
    "updated_at": "2024-01-01T10:00:00Z",
    "activity_logs": [
      {
        "action": "LOGIN",
        "timestamp": "2024-01-01T10:00:00Z",
        "details": {
          "ip": "192.168.0.1"
        }
      }
    ]
  }
}
```

### 1.2 사용자 관리 API

#### 📋 사용자 목록 조회
```http
GET /api/users?page=1&limit=20&status=all&search=검색어&sort_by=created_at&sort_order=desc
```

**Query Parameters:**
- `page`: 페이지 번호 (기본값: 1)
- `limit`: 페이지 크기 (기본값: 20)
- `status`: 사용자 상태 필터 (기본값: all)
- `search`: 검색어 (선택사항)
- `sort_by`: 정렬 기준 (기본값: created_at)
- `sort_order`: 정렬 순서 (기본값: desc)

#### 📊 사용자 통계 조회
```http
GET /api/users/statistics?start_date=2024-01-01&end_date=2024-01-31
```

#### 🔄 사용자 상태 변경
```http
PUT /api/users/{userId}/status
```

**Request Body:**
```json
{
  "status": "SUSPENDED",
  "reason": "정책 위반",
  "duration": 7
}
```

### 1.3 관리자 관리 API

#### ⚙️ 관리자 권한 변경
```http
PUT /api/admins/{adminId}/role
```

**Request Body:**
```json
{
  "role": "SUPER_ADMIN"
}
```

#### 📈 활동 로그 조회
```http
GET /api/admins/activity-logs?page=1&limit=20&admin_id=1&action=LOGIN&start_date=2024-01-01&end_date=2024-01-31
```

### 1.4 신고 관리 API

#### 🚨 사용자 신고 목록 조회
```http
GET /api/reports/users?page=1&limit=20&search=&status=all&sortBy=created_at&sortOrder=desc
```

#### 📝 게시물 신고 목록 조회
```http
GET /api/reports/posts?page=1&limit=20&search=&status=all&sortBy=created_at&sortOrder=desc
```

#### 💬 채팅 신고 목록 조회
```http
GET /api/reports/chats?page=1&limit=20&search=&status=all&sortBy=created_at&sortOrder=desc
```

#### 🔍 신고 상세 조회
```http
GET /api/reports/{reportId}
```

#### ✅ 신고 처리
```http
PUT /api/reports/{reportId}/process
```

**Request Body:**
```json
{
  "status": "RESOLVED",
  "action_taken": "사용자 경고",
  "comment": "처리 완료",
  "notify_reporter": true,
  "notify_reported": true,
  "suspension_duration": 3
}
```

### 1.5 게시물 관리 API

#### 📄 게시물 목록 조회
```http
GET /api/posts?page=1&limit=20&status=all&search=검색어&user_id=userId&sort_by=create_at&sort_order=desc&has_reports=true
```

#### 📖 게시물 상세 조회
```http
GET /api/posts/{postId}
```

#### 🔄 게시물 상태 변경
```http
PUT /api/posts/{postId}/status
```

**Request Body:**
```json
{
  "status": "BLOCKED",
  "reason": "부적절한 내용"
}
```

### 1.6 알림 템플릿 관리 API

#### 📨 알림 템플릿 목록 조회
```http
GET /api/notifications/templates?page=1&limit=20&type=all
```

#### ➕ 알림 템플릿 생성
```http
POST /api/notifications/templates
```

**Request Body:**
```json
{
  "name": "환영 메시지",
  "type": "WELCOME",
  "title": "가입을 환영합니다!",
  "content": "{{username}}님, 가입을 환영합니다!"
}
```

#### ✏️ 알림 템플릿 수정
```http
PUT /api/notifications/templates
```

#### 📤 알림 발송
```http
POST /api/notifications/send
```

**Request Body:**
```json
{
  "recipients": ["user1", "user2"],
  "template_id": "template123",
  "variables": {
    "username": "홍길동"
  },
  "send_immediately": true
}
```

#### 📜 알림 발송 내역 조회
```http
GET /api/notifications/history?page=1&limit=20&start_date=2024-01-01&end_date=2024-01-31
```

### 1.7 Firestore 연동 API

#### 📊 Firestore 문서 조회
```http
GET /api/firestore/collection/{collectionName}/document/{documentId}
```

#### 🔍 Firestore 필드 조회
```http
GET /api/firestore/collection/{collectionName}/document/{documentId}/field/{fieldName}
```

---

## 2. AI Map 서비스 (AI Map Service)

### 2.1 퀘스트 API

#### 🔍 퀘스트 검색
```http
POST /api/quests/search
```

**Request Body:**
```json
{
  "location": {
    "latitude": 37.5665,
    "longitude": 126.9780
  },
  "radius": 1000,
  "category": "RESTAURANT",
  "difficulty": "EASY"
}
```

**Response:**
```json
{
  "quests": [
    {
      "id": "quest1",
      "title": "맛집 탐방",
      "description": "근처 맛집을 찾아보세요",
      "location": {
        "latitude": 37.5665,
        "longitude": 126.9780
      },
      "category": "RESTAURANT",
      "difficulty": "EASY",
      "reward": 100
    }
  ],
  "total_count": 1
}
```

#### 💡 퀘스트 추천
```http
POST /api/quests/recommendations
```

**Request Body:**
```json
{
  "user_id": "user123",
  "location": {
    "latitude": 37.5665,
    "longitude": 126.9780
  },
  "preferences": ["RESTAURANT", "CULTURE"]
}
```

---

## 3. 메시지 서비스 (Message Service)

### 3.1 메시지 API

#### ✉️ 메시지 상태 확인
```http
POST /api/messages/check
```

**Request Body:**
```json
{
  "messageId": "msg123",
  "senderId": "user1",
  "receiverId": "user2",
  "content": "안녕하세요!",
  "timestamp": "2024-01-01T10:00:00Z",
  "isRead": false,
  "chatRoomId": "room123"
}
```

**Response:**
```http
200 OK
```

---

## 4. 알림 서비스 (Notification Service)

### 4.1 FCM 알림 API

#### 🔔 알림 발송 (Kafka 리스너)
알림 서비스는 Kafka 토픽 `notifications`을 통해 메시지를 수신하고 FCM을 통해 푸시 알림을 발송합니다.

**Kafka Message Format:**
```
userId:title:body
```

예시:
```
user123:새 메시지:홍길동님이 메시지를 보냈습니다.
```

---

## 5. 에러 코드

### HTTP 상태 코드

| 상태 코드 | 설명 | 예시 |
|----------|------|------|
| 200 | 성공 | 요청 처리 완료 |
| 400 | 잘못된 요청 | 필수 파라미터 누락 |
| 401 | 인증 실패 | 잘못된 로그인 정보 |
| 403 | 권한 없음 | 관리자 권한 필요 |
| 404 | 리소스 없음 | 존재하지 않는 사용자 |
| 500 | 서버 오류 | 내부 서버 오류 |

### 에러 응답 형식
```json
{
  "success": false,
  "message": "에러 메시지",
  "error_code": "USER_NOT_FOUND"
}
```

---

## 6. 인증 및 권한

### JWT 토큰
- 모든 API 요청에는 Authorization 헤더에 JWT 토큰이 필요합니다.
- 토큰 형식: `Bearer {token}`

### 권한 레벨
- `USER`: 일반 사용자
- `ADMIN`: 관리자
- `SUPER_ADMIN`: 최고 관리자

---

## 7. 데이터 형식

### 날짜/시간
- ISO 8601 형식 사용: `2024-01-01T10:00:00Z`

### 페이지네이션
```json
{
  "data": [...],
  "pagination": {
    "current_page": 1,
    "total_pages": 10,
    "total_items": 100,
    "per_page": 20
  }
}
```

### 좌표
```json
{
  "latitude": 37.5665,
  "longitude": 126.9780
}
```

---

## 8. 환경 설정

### 개발 환경
- Base URL: `http://localhost:8080`
- Eureka Server: `http://localhost:8761`

### 테스트 환경
- Base URL: `http://211.187.162.65:8080`
- Eureka Server: `http://211.187.162.65:8761`

---

## 9. Kafka 토픽

| 토픽 이름 | 용도 | 프로듀서 | 컨슈머 |
|----------|------|----------|---------|
| `notifications` | 알림 발송 | User Auth Service | Notification Service |
| `chat.unread` | 읽지 않은 메시지 | Message Service | Notification Service |

---

## 10. 버전 정보

- API 버전: v1.0
- 마지막 업데이트: 2024-01-01
- Spring Boot 버전: 3.2.3
- Java 버전: 17

---

## 11. 연락처

개발팀 문의: dev@taptalk.com 
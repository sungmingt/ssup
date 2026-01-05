## 🔐 인증 및 공통 가이드
ssup API의 공통 인증 절차
                
### 1. 인증 절차
- 모든 인증이 필요한 API에는 인증 정보(ATK 쿠키) 검증을 수행합니다.
- 각각 ATK/RTK 쿠키가 존재합니다.

#### 인증이 필요한 API
- ATK가 없는 경우: status:`401`, code: `LOGIN_REQUIRED`를 응답합니다.
- ATK가 만료된 경우: status:`401`, code: `TOKEN_EXPIRED`를 응답합니다.

#### 토큰의 최초 발급
- 일반 로그인 또는 OAuth2 소셜 로그인에 성공하면 ATK/RTK 쿠키가 발급됩니다.

#### ATK/RTK의 TTL
- Access Token: 30분
- Refresh Token: 7일

### 2. 인증 상태 확인 (`/api/auth/me`)
- **용도**: 앱 진입 시 또는 새로고침 시 현재 인증 상태를 확인합니다.
- **동작**: 유효한 Access Token이 있으면 유저 정보를 반환하고, 없으면 null을 반환합니다.

### 3. 토큰 만료 및 재발급
- **401 Unauthorized**: 토큰이 만료되었거나 없는 경우 필수적으로 발생합니다.
- 응답 status가 401이고, body의 `code`가 `TOKEN_EXPIRED`인 경우 `/api/auth/reissue`를 통해 재발급을 시도해야합니다.
 
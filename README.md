# internship-java-onboarding
Spring Security와 JWT를 사용하여 사용자 인증/인가

## 🔗API
### 1. 회원가입
🚨 회원가입 시 username, password, nickname 입력 필수
- **HTTP Method**: `POST`
- **URL**: `/signup`
- **Content-Type**: `application/json`
- **Request Body**
```json
{
	"username": "JIN HO",
	"password": "12341234",
	"nickname": "Mentos"
}
```
- **Response Body**
```json
{
	"username": "JIN HO",
	"nickname": "Mentos",
	"authorities": [
            {
              "authorityName": "ROLE_USER"
            }
    ]
}
```
### 2. 로그인
🚨 로그인 시 username, password 입력 필수
- **HTTP Method**: `POST`
- **URL**: `/signin`
- **Content-Type**: `application/json`
- **Request Body**:
```json
{
    "username": "string",
    "password": "string"
}
```
- **Response Body**
```json
{
	"token": "토큰값"
}
```


### 3. 액세스 토큰 재발급
🚨 **Authorization** 헤더에 **Bearer {refresh_token}** 값을 입력하여, **Refresh Token**이 유효한 경우 **Access Token**을 재발급
- **HTTP Method**: `POST`
- **URL**: `/refresh-token`
- **Content-Type**: `application/json`
- **Request Header**: `Authorization: Bearer {refresh_token}`
- **Response Body**
```json
{
  "access-token": "토큰값",
  "refresh-token": "토큰값"
}
```


## 🌏 배포
**Swagger UI** <br>
http://43.201.244.7:8080/swagger-ui/index.html

### 블로그 API 만들기

#### 미션
- 블로그 글 CRUD API 구현

#### 기능
- 블로그 글 작성
- 블로그 글 조회 (단건 조회 / 전체 목록 조회)
- 블로그 글 삭제
- 블로그 글 수정

#### 활용 기술
- 스프링 부트
- 스프링 데이터 JPA
- 롬복 (Lombok)
- H2

#### 프로젝트 구조
```
springboot-developer
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── me.blogSpringBoot
│   │   │       └── springbootdeveloper
│   │   │           ├── config
│   │   │           │   └── WebSocketConfig
│   │   │           ├── controller
│   │   │           │   ├── BlogApiController
│   │   │           │   └── WebSocketController
│   │   │           ├── domain
│   │   │           │   └── Article
│   │   │           ├── dto
│   │   │           │   ├── AddArticleRequest1
│   │   │           │   ├── ArticleResponse
│   │   │           │   ├── UpdateArticleRequest
│   │   │           │   └── RandomMessageDTO
│   │   │           ├── repository
│   │   │           │   └── BlogRepository
│   │   │           ├── service
│   │   │           │   └── BlogService
│   │   │           ├── security
│   │   │           │   └── ServerSecurityConfigration
│   │   │           └── websocket
│   │   │               ├── WebSocketErrorHandler
│   │   │               └── UserChannelInterceptor
│   │   │
│   │   └── resources
│   │       ├── application.yml
│   │       ├── application_test.yml
│   │       └── data.sql
│   │
│   └── test
│       ├── java
│       │   └── me.blogSpringBoot
│       │       └── springbootdeveloper
│       │           ├── controller
│       │           │   └── BlogApiControllerTest
│       │           └── JUnitTest
│       │               ├── JUnitCycleTest
│       │               └── JUnitTest
│       │
│       └── resources
│
```
#### 엔티티 구성
|컬럼명|자료형| null 허용 | 키  | 설명 |
|:-----|:------|:--------|:---|:-----|
| id | BIGINT | N       | PK | 일련번호 |
| title | STRING | N    |    | 게시물의 제목 |
| content | STRING | N  | 내용 |

#### 작업 순서
1. 엔티티 구성하기 (Article)
2. 레파지토리 만들기 (BlogRepository)
3. API 구현하기 1 (서비스 메서드 코드 작성하기)
4. API 구현하기 2 (컨트롤러 메서드 코드 작성하기)
5. API 실행 테스트 하기

#### H2 데이터베이스 접근
- http://localhost:8080/h2-console/
- JDBCURL 확인 ( jdbc:h2:mem:testdb )

#### 테스트 코드 작성하기
- Given : 블로그 글 추가에 필요한 요청 객체 만들기
- When : 블로그 글 추가 API에 요청 보내기. 요청 타입은 JSON이며, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보내기
- Then : 응답 코드가 201 Created인지 확인하기. Blog를 전체 조회해 크기가 1인지 확인하고, 실제로 저장된 데이터와 요청값 비교하기
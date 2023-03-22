# 블로그 검색 서비스

> 카카오,네이버 REST API를 사용하여 블로그 검색내용을 가져오고 검색어를 저장하여 인기 검색어를 조회 할 수 있는 서비스.
---
### ENV

- JDK 17
- Spring Boot 2.7.9
---
### 실행방법

아래 jar 파일을 다운로드 받은 뒤 아래 코드로 실행한다.

https://github.com/devkjc/blog-search/raw/main/blog-search-api.jar

>```
>cd {다운로드 경로}
>
>java -jar blog-search-api.jar
>```
---
### API 문서
> [Swagger-UI] http://localhost:3599/swagger-ui/index.html
---

### 외부 라이브러리 및 오픈소스

- it.ozimov:embedded-redis:0.7.3
> ```
> 트래픽과 동시성 이슈 해결을 위한 Redis 사용을 위한 Embeded-Redis
> ```
- io.springfox:springfox-boot-starter:3.0.0
- io.springfox:springfox-swagger-ui:3.0.0
> ```
> API 문서 작성을 위한 Swagger
> ```
- com.squareup.okhttp3:mockwebserver
> ```
> WebClient Mock Test를 위한 mockwebserver
> ```
- org.projectlombok:lombok
> ```
> 코드 간략화 및 편의성을 위한 Lombok
> ```

---


# SpringBoot Server 프로젝트 개발 가이드

Springboot 를 사용하여 서버를 개발하기 공통 모듈과 개발 가이드를 제공한다.
본 프로젝트 템플릿은 중/소 규모 백엔드 API 서버 개발에 최적화 되어 있다.

## 개발 환경 설정

패키지명을 변경하여 사용하여야 한다. 다음에서 "com.vrerv.springboottemplate.server" => "com.mycompany.myproject" 로 변경한다.

* build.gradle
* src/main/java
* src/test/groovy
* src/test/java


### 개발 환경 설명

#### OS

최신 리눅스나 Mac OS X 에서 개발할 것을 추천한다. Windows OS 의 경우 10 이상 버전을 사용하고 WSL2 환경에서 개발하는 것을 추천한다.
본 문서에서는 해당 환경에서 작동하는 것을 가정한다. 만약 Windows OS 에서 WSL 을 사용하지 않을 경우 일부 기능이 작동되지 않을 수 있다.

* [Windows 에서 WSL2 설치](https://learn.microsoft.com/ko-kr/windows/wsl/install)

#### 필요한 소프트웨어

* Sdkman - Java 개발을 위한 소프트웨어를 설치하고 관리한다.
* Java 17 - 코드를 빌드하고 구동한다.
* Git - 소스 코드를 관리한다.
* Docker - 배포용 이미지를 생성하고 로컬에 설치하면 구동한다.
* IntelliJ - 코드를 개발한다. 개발자의 역량에 따라 다른 개발툴을 사용하여도 무난하다.
* MySQL DB - 8.0.32 이상을 로컬에 설치하여, 로컬 개발시 사용한다.

####

### Java 설치

* java 17 설치 - [sdkman](https://sdkman.io/) 으로 설치한다.
* `~/.sdkman` 디렉토리를 `/opt/sdkman` 으로 링크한다. (Windows 의 경우 WSL 사용)
  만약 디렉토리를 링크하거나 바꿀수 없거나 다른 곳으로 설정한다면, `gradle.properties` 안에 jdk 경로를 수동으로 설정해주어야 한다.
  해당 파일은 git 에 관리되는 파일로 되어 있으므로 되도록 기존 디렉토리 구조를 따르도록 한다.

```bash
sdk install java 17.0.5-amzn
```

### MySQL 준비

* MySQL 을 local 에 설치한다. 
* 로컬 구동을 위한 데이터베이스와 계정을 생성한다.
```sql
CREATE DATABASE test_db;
CREATE USER 'test_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'test_user_password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, DROP, ALTER, REFERENCES, CREATE TEMPORARY TABLES, LOCK TABLES ON test_db.* TO 'test_user'@'localhost';
GRANT FILE ON *.* TO 'test_user'@'localhost';
FLUSH PRIVILEGES;
```

### IntelliJ 에서 개발

* 프로젝트 폴더의 root (server directory) 를 IntelliJ 에서 오픈한다.
* 기본 JDK 설정이 17이 아니라면 프로젝트 JDK 를 설치된 Java 17 으로 설정한다.
* 설정에서 gradle 검색후 gradle 구동 JDK 를 "Project SDK" 로 설정한다.

### 명령행에서 개발

빌드: 
`./gradlew clean buid`

구동: 
`./gradlew bootRun`

## 개발 상세

### 개발 프로젝트 구조

* src/main/java - Java 소스 코드
* src/main/resources - Java 프로젝트 설정 파일
* src/test/java - Java 테스트 코드
* src/test/resources - Java 테스트 코드 설정 파일
* src/test/http - IntelliJ http client 테스트 파일
* src/test/grovvy - Groovy Spock 테스트 코드

#### 패키지 구조

* {basePackagePrefix}.{module}.{layer} - 기본 패키지 구조
* {module} - common(공통), 그외 관련 모듈이름
* {layer}
    * domain - 도메인 모델 관련 레이어
    * application - 서비스, 비즈니스 로직 레이어
    * presentation - 웹 API, UI 레이어

각 레이어는 정확히 DDD(Domain Driven Develop)의 레이어와는 일치하지 않는다.
SpringFramework, hibernate, String data JPA 등에 강하게 연결되어 있기 때문이다.
다만, 각 레이어간에 정형화를 통해 개발과정을 단순화하고 일관성을 유지하기 위해 레이어를 정의하였다.

도메인 레이어 <- 애플리케이션 레이어 <- 프레젠테이션 레이어 순으로 의존성이 존재한다.
모듈 간의 호출에서는 presentation 레이어의 Endpoint 로 호출한다.

##### 도메인 레이어

* entity 와 repository 를 주로 다룬다, DB 설계와 도메인 모델의 기본 룰을 설정한다.

##### 애플리케이션 레이어

* 비지니스 로직을 수행한다.

##### 프레젠테이션 레이어

* 웹 API, UI 레이어
* Spring controller 등 

#### 클래스 명명

특정 타입에 대한 클래스명 접미사

* `Repo` - spring data repository
* `Service` - 비지니스 로직 수행
* `Endpoint` - 웹 API endpoint
* `Model` - DTO or VO
* `Mapper` - MapStruct mapper or Java Bean converter
* `Config` - Spring configuration or any 인프라 설정

### Web API 개발 방법

#### Authentication

* JWT 토큰 인증을 사용한다.

#### Web API 문서화

* SpringDoc 이 적용되어 서버의 [Swagger UI](http://localhost:8080/swagger-ui.html) 로 서버에 존재하는 API 확인할 수 있다.
* API 의 명세는 `build` 시 `openapi.json` 으로 자동 생성된다.

#### API client project 생성

[openapi-generator](https://github.com/OpenAPITools/openapi-generator-cli) 를 사용하여 API client project 를 생성할 수 있다.
CLI 는 여러가지를 지원함으로 필요한 CLI 를 설치해서 사용한다. 아래는 docker 를 사용한 예이다.

아래 docker 명령으로 API client project 를 생성할 수 있다.(server directory 에서 실행)
`-i /local/app/openapi.json` 대신에 `http://localhost:8080/v3/api-docs` 
(docker 로 실행시에는 ip가 컨테이너 내부에서 host 를 바라보도록 해야 함) 를 사용할 수도 있다.

```bash
export PACKAGE_BASE_NAME=com.vrerv.springboottemplate.server
docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
    -i /local/app/openapi.json \
    -g java --library retrofit2 \
    --group-id ${PACKAGE_BASE_NAME} \
    --artifact-id api-client-java \
    --package-name ${PACKAGE_BASE_NAME}.client \
    --api-package ${PACKAGE_BASE_NAME}.client.api \
    --model-package ${PACKAGE_BASE_NAME}.client.api.model \
    --invoker-package ${PACKAGE_BASE_NAME}.client \
    -o /local/api-client-java
```

javascript api client library 생성은 아래와 같이 하면 된다.

```bash
export PACKAGE_BASE_NAME=vrerv.springboottemplate
docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
    -i /local/app/openapi.json \
    -g javascript \
    --group-id ${PACKAGE_BASE_NAME} \
    --artifact-id api-client-java \
    --package-name ${PACKAGE_BASE_NAME}.client \
    --api-package ${PACKAGE_BASE_NAME}.client \
    --model-package ${PACKAGE_BASE_NAME}.client \
    --invoker-package ${PACKAGE_BASE_NAME}.client \
    -o /local/api-client-javascript
```

### DB 레이어 개발

* 모든 `Entity` 는 `Long id` 필드를 갖는 `EntityId` 클래스를 상속하여 작성한다.
* `Setter` 는 업데이트가 가능한 필드만 `public` 으로 선언한다.

#### MapStruct 를 통한 VO, DTO, Entity 변환

* `MapStruct` 를 사용하여 `DTO`, `Entity` 또는 `VO` 간에 서로 변환한다.

### Test 코드 작성

#### IntelliJ Http Client

[IntelliJ http client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html) 를 사용하여 수동 테스트를 처리할 수 있다.
다른 api 테스트 툴을 사용한다면, `openapi.json` 을 해당 툴에 임포트 하여 쉽게 생성할 수 있을 것이다.

API 추가/수정/삭제시 해당 .http 파일을 업데이트한다. 본 테스트는 개발자에 의한 수동 테스트를 위한 것으로 개발중에 
개발자가 필요에 따라 추가하여, API 호출을 테스트할 수 있다.

#### Spock Test

* [Spock](https://spockframework.org/spock/docs/2.3/index.html) 을 사용하여 테스트 코드를 작성한다.

## Tech Stack

* Java 17 - [Amazon Corretto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
* Gradle 7.6.1
    * [SpringDoc OpenApi Plugin](https://github.com/springdoc/springdoc-openapi-gradle-plugin)

* Springboot 2.7.10
    * Spring-web
    * Spring-security
    * Spring-doc
    * Spring-data
* Lombok
* MapStruct - bean mapper [Reference](https://mapstruct.org/documentation/stable/reference/html/)
* JWT token authentication
* Testing
    * JUnit5
    * Spock

* MySQL DB 8.0.32

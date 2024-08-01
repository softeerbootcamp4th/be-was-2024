# be-was-2024
# URL Parsing하기

Socket은 사용자의 연결을 위한 클래스.

ServerSocket은 서버의 연결을 위한 클래스.

```java
Socket connection;
connection.getInputStream();
```

을 이용해 사용자의 요청을 모두 읽어들일 수 있다.

```java
BufferedReader br = new BufferedReader(new InputStreamReader(in));

String line;
while(!(line = br.readLine().isEmpty()) {
	log.debug(line);
}
```

을 이용해 요청을 모두 읽은 후 출력할 수 있다.

# 파일 Byte Array로 읽어오기

## 1. Files.readAllBytes

java의 nio패키지를 이용하여 다음과 같이 파일을 읽어올 수 있다.

```java
byte[] body = Files.readAllBytes(new File("파일 경로").toPath()); 
```

## 2. FileInputStream.read(byte[] b);

java io의 FileInputStream을 이용하여 byte array로 읽는게 가능하다.

```java
File html = new File("src/main/resources/static" + url);
byte[] body = new byte[(int) html.length()];

// 버퍼를 사용하면 빠르게 가져올 수 있음
try (FileInputStream fileInputStream = new FileInputStream(html);
BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);) {
        bufferedInputStream.read(body);
}
```

# Concurrent 패키지

작업을 스레드에 할당시켜 실행하면 큰 작업을 나누어 실행시켜 빠르게 작업을 완료할 수 있다는 장점이 있다. 하지만 작업을 수행할 때마다 스레드를 생성하고, 스레드를 종료한다면 그에 따른 오버헤드가 커져 오히려 실행 시간이 더 오래 걸릴 수 있다.

Thread.start()가 아닌 Executor와 ExecutorService를 이용하여 더 효율적으로 스레드를 관리할 수 있다.

Executor와 ExecutorService는 스레드 풀의 구현을 위한 인터페이스이다. 등록된 작업(Runnable)을 실행하는 기능을 가지고 있다.

ExecutorService를 이용하면 스레드 풀(Thread Pool)을 관리할 수 있다.

### Executor의 구조

![img.png](img.png)
ExecutorService 내부에는 블로킹 큐와 스레드 풀을 가지고 있다. 스레드 풀 개수만큼 작업을 할당한 후 추가로 작업이 들어오면 블로킹 큐에 작업을 넣어 기다리게 한다.

예를 들어 스레드 풀의 크기가 3인 상태에서 T1, T2, T3, T4 4개의 작업이 들어오면 들어온 순서대로 3개의 작업을 스레드 풀에 할당한 후, 나머지 하나의 작업은 블로킹 큐에서 기다리게 된다. 앞의 작업이 완료되어 빈 스레드가 생기면 기다리던 작업을 할당받아 작업을 수행한다.

ExecutorService는 Executor의 상태를 관리하기 위한 메소드들도 가지고 있다.

- shutdown
    - 새로운 작업을 받아들이지 않음
    - 제출된(현재 실행중인) 작업들은 그대로 종료
- shutdownNow
    - shutdown에 추가해서 현재 실행중인 작업들에 인터럽트를 걸어줌
    - 대기중인 작업 목록 반환
- isShutdown
    - Executor의 셧다운 여부 반환
- isTerninated
    - shutdown실행 후 모든 작업의 실행이 종료되었는지 여부 반환
- awaitTermination
    - shutdown실행 후 지정한 시간동안 작업이 종료될 때까지 기다림
    - 지정한 시간 내에 작업이 모두 종료되었는지 여부 반환

작업이 완료되었다면 shutdown을 반드시 호출하여 종료시켜주자. 그렇지 않으면 계속 작업을 기다리게 됨.

Executor, ExecutorService, ScheduledExecutorService는 스레드 풀 관리를 위한 인터페이스이다. 하지만 이를 직접 관리하는 것을 어렵다. 관리의 편의를 위해 Executors라는 팩토리 클래스가 존재한다. 스레드 풀을 손쉽게 생성해준다.

- newFixedThreadPool
    - 고정된 크기의 스레드 풀 생성
    - ExecutorService를 구현한 ThreadPoolExecutor 객체가 생성됨
- newCachedThreadPool
    - 알아서 필요한 만큼의 스레드 풀 생성
    - 이미 생성된 스레드 재활용 가능
- newScheduledThreadPool
    - 일정 시간 뒤 혹은 주기적으로 실행되어야 하는 스레드 풀을 생성
    - ScheduledExecutorService를 구현한 ScheduledThreadPoolExecutor 객체가 생성됨
- newSingleThreadExecutor, newSingleScheduledExecutor
    - 크기가 1인 스레드 풀 생성

코드로는 다음과 같이 작성한다

```java
ExecutorService executorService = Executors.newCachedThreadPool();
executorService.execute(new RequestHandler(connection));
// RequestHandler는 Runnable 인터페이스를 구현한 클래스
// run() 메소드를 가지고 있음
```

인터럽트에 대한 예외를 처리하기 위해 다음과 같이 작성한다

```java
public void run() {
	try {
		while (!Thread.currentThread.isInterrupted()) {
			// 작업
		}
	} catch (InterruptedException e) {
		logger.error(e.getMessage());
	}
}
```

---
# MIME이란?

MIME(Multipurpose Internet Mail Extensions)은 문서, 파일, 바이트 집합의 성격을 나타낸다.

메일을 보낼 때 텍스트 이외의 다른 파일(바이너리 파일)을 함께 전송하기 위해 고안되었다. 형식은 `type/subtype` 형태로 나타나며 header의 `content-type` 필드에 담아서 보내게 된다. `type`은 형식, `subtype`은 포맷을 의미한다. 예를들어 html 파일을 전송하고자 한다면 `content-type: text/html` 으로 보내는 방식. 이를 위해 서버에서는 사용자가 요청한 파일이 어떤 타입의 파일인지 알아야 하고, 이 타입에 따라 `content-type`을 설정해서 응답해주어야 한다.

http response header에서 다음과 같이 나타남.

```java
content-type: type/subtype;parameter=value
```

## 타입

- discrete: 단일 텍스트, 오디오, 음악파일 등 단일 파일이나 매체
  - application: 밑의 다른 타입 중 하나에 명시적으로 속하지 않는 모든 종류의 바이너리 데이터. 다른 방식으로 실행되거나 해석될 수 있음. 특정 어플리케이션을 사용하는 경우도 이에 해당함. `application/pdf`, `application/zip` 등이 있음.
  - audio: 오디오 또는 음악 데이터
  - example
  - font: 글꼴
  - image: 비트맵, 이미지, GIF
  - model: 3D 객체 또는 화면에 대한 모델 데이터
  - text: 사람이 읽을 수 있는 콘텐츠, 소스코드 등. `text/plain`, `text/html` 등이 있음
  - video: 비디오 데이터
- multipart: 여러 컴포넌트 조각으로 구성된 문서
  - message: 다른 메시지를 캡슐화함. 전달된 메시지를 인용하거나, 큰 메시지를 여러 메시지인 것처럼 나누어 보낼 수 있음. `message/partial`을 이용하면 큰 메시지를 작은 메시지로 나누어서 수신자가 재조립할 수 있도록 함.
  - multipart: 여러 컴포넌트로 구성된 데이터. 각 컴포넌트는 다른 MIME 타입을 가질 수 있음. `multipart/form-data`나 `multipart/byteranges`가 있음.

### 알 수 없는 타입

- 알 수 없는 이진 파일인 경우: `application/octet-stream`
- 알 수 없는 텍스트 파일인 경우: `text/plain`

## MIME 타입의 중요성

브라우저는 http response의 MIME타입을 보고 데이터를 디코딩함. css파일의 MIME 타입을 `text/html`로 설정하여 response하면 브라우저에서 정상적으로 동작하지 않기 때문에 정확히 설정해줄 필요가 있음. 또한 기본 타입인 `application/octet-stream`으로 알려지지 않은 타입의 리소스를 전송하면 대부분 브라우저는 보안상의 이유로 허용하지 않음.

### MIME 스니핑

MIME 타입이 없거나 브라우저가 MIME 타입이 올바르지 않다고 판단하는 경우 리소스의 바이트를 보고 올바른 MIME 타입을 추측함.

예를 들어 safari 브라우저의 경우 MIME 타입이 정확하지 않다면 URL파일의 확장자를 확인함.

## content-type이란?

http request 또는 http response header에서 body에 들어있는 데이터 타입에 대한 정보를 나타냄.

MIME 타입의 하나. MIME 타입으로 지정된 타입들을 content-type 필드에 실어서 보냄.

## content-type 반환을 위한 http request와 http response의 흐름

1. http request에서 원하는 파일을 request한다.
2. url parser에서 request 파일의 확장자를 파싱한다.
3. request 파일의 확장자를 이용하여 http response의 content-type을 알아낸다.
  1. 이를 위해 각 확장자에 대한 `content-type`이 미리 정의되어 있어야 한다. enum을 이용함.
4. response200Header에 매개변수로 content-type을 넘겨준다. 이를 response header에 포함하여 response 해준다.

response200Header의 코드

```java
private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
    try {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    } catch (IOException e) {
        logger.error(e.getMessage());
    }
}
```

### enum을 이용한 구현

```java
enum TYPE {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    JSON("json", "application/json"),
    ICO("ico", "image/vnd.microsoft.icon"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    JPG("jpg", "image/jpeg");

    private final String type;
    private final String mime;

    TYPE(String type, String mime) {
        this.type = type;
        this.mime = mime;
    }

    public String getType() {
        return this.type;
    }

    public String getMime() {
        return this.mime;
    }
}
```

이후에 ResourceHandler에서 for문을 이용하여 TYPE을 순회하며 request에서 받아온 확장자에 해당하는 mime 타입을 알아낸다.

### (번외) Accept Header

Accept Header는 request를 보낼 때 브라우저에서 허용할 타입 목록을 보내는 헤더를 말한다. content-type과 마찬가지로 MIME 타입 목록을 보낸다. 예를들어 `Accept: text/html` 을 보낸다면 “나는 html만 처리할 예정이니 html 파일을 보내줘” 라고 말하는 것과 같다.

---
# 정적 팩토리 메서드 패턴

객체를 생성할 때 생성자를 이용해도 좋지만 다른 방법을 이용하여 객체를 생성할 수 있다. 객체를 만들어주는 클래스의 메소드 라고 생각하면 된다.

### 기본 형태

```java
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
    
    protected User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    
    public static User from(HashMap<String, String> data) {
        return new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
    }
}
```

위 코드에서

```java
User user = USER.from(userData);
// userData는 사용자 정보를 가진 해시맵
```

를 이용해 객체를 생성할 수 있다.

파싱한 쿼리스트링은 쿼리스트링마다 포함하고 있는 데이터의 양이 다르다. 이를 해시맵으로 만든 후 정적 팩토리 메서드를 이용하면 쉽게 처리할 수 있다.

### 생성자와의 차이

- 이름을 가질 수 있다.
  - 위 예제에서는 from이라는 이름을 가진다.
- 호출할 때마다 새로운 생성자를 호출할 필요가 없다.
  - 스태틱 메서드를 호출하면 된다.
- 하위 자료형 객체를 반환할 수 있다.
  - 만약 어떤 클래스를 상속받은 여러 클래스가 존재한다면 from 함수에서 이를 처리해줄 수 있다.

    ```java
    public static User from(String job) {
    		if (job.equals("student") {
    			return new Student();
    		} else if (job.equals("professor") {
    			return new Professor();
    		} else {
    			return new User();
    		}
    }
    ```

  위와 같이 구현하면 job에 따른 객체를 생성할 수 있다. 이 때 Professor 클래스와 Student 클래스는 User클래스를 상속하고 있어야 한다.

- 객체 생성을 캡슐화할 수 있다.

    ```java
    User user = USER.from(userData);
    User user = new User("qwer", "asdf", "zxcv", "wwww");
    ```

  위와 아래의 생성 방식에서 아래를 채택하면 생성자의 내부 구조가 그대로 노출된다. 정적 메서드 패턴을 사용하면 생성자의 내부 구조를 캡슐화할 수 있다는 장점이 있다.


# GET 처리하기

request line을 읽어서 공백을 기준으로 split한 후 가장 첫 번째 값이 http method가 된다. 이를 이용해 http method에 대한 처리를 구현할 수 있다.

RequestHandler에서 request객체를 만든 후에 getHttpMethod()를 호출하여 메소드를 알아낸다. 이후 메소드에 따른 처리를 구현한다.

```java
Request request = Request.from(requestLine);
String method = request.getHttpMethod();

if (method.equals("GET")) {
		if (request.isQueryString()) {
				logicProcessor.createUser(request);
				response.redirect("/index.html", dos);
		} else {
				response.response(request.getStaticPath(), dos);
		}
}
```

이후에 다른 여러 http method (POST, DELETE, UPDATE…)등에 대한 처리를 위해 정적 스태틱 메서드를 이용한 Response 객체 생성을 고려해볼 필요가 있다.

# Request, Response 객체 분리하기

## Request 객체

Request 객체는 request에 관한 모든 것을 처리한다. 생성자의 매개변수로 request line을 전달한다. 생성자 내부에서 split을 이용하여 첫 번째 값은 method, 두 번째 값은 path에 저장한다. 또한 쿼리스트링을 포함하고 있는 요청인지 확인하기 위해 isQueryString() 메소드를 가지고 있다. 또한 기존의 RequestHandler에서 담당하던 response 관련 메소드들을 모두 가지고 있다.

기본 구조는 다음과 같다.

```java
public class Request {
    private final String method;
    private final String path;

    protected Request(String requestLine) {
        this.method = requestLine.split(" ")[0];
        this.path = requestLine.split(" ")[1];
    }

    public static Request from(String line) {
        return new Request(line);
    }
    
    ...
}
```

## Response 객체

### Redirect

로그인이 완료되면 메인 화면으로 돌아가도록 구현해야 한다. 이는 리다이렉트를 이용한다.

http header에서 리다이렉트 코드는 301, 302 2가지가 있다.

- `301 Moved Permanently`: 영구적인 리다이렉트. 이전 페이지의 데이터를 전혀 포함하지 않은 채로 Location필드의 path로 이동한다.
- `302 Found`: 캐시된 상태로 리다이렉트. 이전 페이지의 정보를 캐싱한 상태로 다음 페이지로 이동한다. 로그인 시에 리다이렉트 되는 경우는 보통 302를 사용한다.

리다이렉트를 위해 redirect라는 메소드를 가지고 있다. 리다이렉트를 위해선 http response header에 Location 필드를 추가해주어야 한다. 로그인 시 이전 페이지의 정보는 필요하지 않다고 생각해서 301 redirect로 구현하였다. 코드는 다음과 같다.

```java
private void response301Header(DataOutputStream dos, String newLocation) {
    try {
        dos.writeBytes("HTTP/1.1 301 Moved Permanently \r\n");
        dos.writeBytes("Location: " + newLocation + "\r\n");
        dos.writeBytes("Content-Length: 0\r\n");
        dos.writeBytes("\r\n");
    } catch (IOException e) {
        logger.error(e.getMessage());
    }
}
```

content-type 등 파일을 반환하는 것이 아니기 때문에 그에 관한 필드는 작성하지 않아도 된다.

redirect가 되면 다시 처음부터 `/index.html`을 로딩하게 된다.

---
# POST로 회원가입

http method를 POST로 설정하여 요청을 보내면 request에 body가 추가되어 전송된다.

이 때 body에는 String형 데이터가 올 수도 있지만, 이미지나 오디오같은 byte형 데이터가 올 수도 있다. 서버에서 BufferedReader를 이용해 body를 String으로 읽는다면 추후 파일 처리에서 오류가 발생할 가능성이 높다. 이를 위해 body를 byte로 읽을 필요가 있다.

1. InputStream을 byte로 읽는다.

```java
// ByteArrayOutputStream()은 byte데이터를 가변적으로 넣을 수 있는 객체이다.
ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();

// read()메소드를 이용하여 1바이트씩 읽는다. 
// 해당하는 바이트는 0~255 정수 중 해당하는 정수로 변환되어 저장된다.
int curr = inputStream.read();
```

1. header와 body 사이에는 빈 줄이 한 줄 있다. 이를 이용하여 구분한다.
  1. 공백문자는 “\r\n”으로 나타난다.
  2. 공백문자가 연속으로 두 번 나타났다면 그 지점의 인덱스를 이용하여 header와 body를 구분한다.
  3. 윗부분은 request line + header, 아랫 부분은 body로 설정한다.
2. request line과 request header 부분은 String으로 변경하여 기존과 같은 방법으로 파싱한다.
3. body 부분은 byte array로 보관하거나 요청 타입에 따라 String으로 변환하여 보관할 수 있다.
  1. header 부분에서 Content-Length 필드가 존재한다면 이를 이용해 byte array를 생성한다.
  2. Content-Length 필드는 POST 요청이라면 존재하지만 GET 요청이라면 존재하지 않을 수도 있음. GET 요청에서 존재하는 경우 필드의 값이 0.
  3. 남은 byte 부분을 모두 저장한다.

    ```java
    // Content-Length 값 추출하기
    String contentLengthValue = headers.get("Content-Length");
    if (contentLengthValue != null) {
    		contentLength = Integer.parseInt(contentLengthValue);
    }
    // 크키만큼 byte array 생성
    this.byteBody = new byte[contentLength];
    
    // 남은 byte 읽기
    int bytesRead = 0;
    while (bytesRead < contentLength) {
    		int result = inputStream.read(byteBody, bytesRead, contentLength - bytesRead);
    		if (result == -1) break; // 스트림 끝
    		bytesRead += result;
    }
    ```


# PostDistributor

POST 요청의 “/user/create” url로 회원가입 요청이 들어온다면 logicProcessor의 createUser 메소드 실행

```java
String path = request.getPath();
if (path.equals("/user/create")) {
		logicProcessor.createUser(request);
		response.redirect("/index.html", dos, 302);
}
```

# GetDistributor

GET요청의 “/user/create” url로 회원가입 요청이 들어온다면 404 Not Found 반환
```java
String path = request.getPath();
if (path.equals("/user/create")) {
        response.redirect("/not_found.html", dos, 404);
}
```

---
# 쿠키

## 쿠키란?

쿠키란 서버가 사용자의 브라우저에 전송하는 작은 데이터 조각이다. 사용자를 식별하기 위해 사용된다.

HTTP는 Stateless connection이다. 하지만 상태를 저장할 때가 더 효율적인 경우가 있다. 대표적인 예는 로그인이다. 만약 상태를 저장할 수단이 없다면 쇼핑몰에서 장바구니에 상품을 담을 때마다 로그인을 해야할 것이다.

주로 다음과 같은 세 가지 목적을 위해 사용됨

- 세션 관리: 서버에 저장해야 할 로그인, 장바구니, 게임 스코어 등의 정보 관리
- 개인화: 사용자 선호, 테마 등의 세팅
- 트래킹: 사용자 행동을 기록하고 분석하는 용도

## 쿠키 만들기

서버는 응답과 함께 header에 쿠키에 대한 정보를 담아서 응답한다. 필드 이름은 Set-Cookie

```java
Set-Cookie: <cookie-name>=<cookie-value>
```

예시

```java
HTTP/1.0 200 OK
Content-type: text/html
Set-Cookie: userId=qwer; password=1234

[page content]
```

사용자는 서버에 요청을 보낼 때 쿠키에 대한 정보를 담아서 요청한다. 필드 이름은 Cookie

```java
GET /sample_page.html HTTP/1.1
Host: www.example.org
Cookie: userId=qwer; password=1234
```

## 쿠키의 라이프타임

쿠키는 Session쿠키와 Permanent쿠키가 있다. 위에서 살펴본 쿠키는 모두 Session쿠키.

Session쿠키는 브라우저를 종료하면 삭제된다.

Permanent 쿠키는 쿠키가 살아있는 기간을 지정하여 브라우저가 종료되어도 삭제되지 않는다. 정해진 기간이 지나면 삭제된다. 다음과 같이 설정할 수 있다. Expires나 Max-Age 옵션을 이용한다.

```java
Set-Cookie: userId=qwer; password=1234; Expires=Wed, 26 Oct 2022 07:28:00 GMT;
```

## 보안상의 문제

아래와 같이 사용자 정보를 쿠키에 담아서 보낼 수 있다.

```java
Set-Cookie: userId=qwer; password=1234
```

위와 같은 경우 쿠키를 탈취하기만 하면 사용자의 로그인 정보를 고스란히 알 수 있다는 보안상의 문제가 있다.

따라서 서버에서는 세션(Session)이라는 것을 사용한다.

## Session

위에서 설명한 보안상의 문제를 해결하기 위해 Session이 도입되었다.

사용자의 정보는 서버에서만 관리하고 사용자에게 전송하지 않는다. 대신 사용자에게는 session id를 전송한다. 사용자는 session id를 가지고 있다가 요청 시 서버에 전송하면 서버는 session id를 이용하여 필요한 사용자 정보를 session 목록에서 알아낸다.

```java
HTTP/1.1 200
Set-Cookie: JSESSIONID=FDB5E30BF20045E8A9AAFC788383680C;
```

## Session의 동작 순서

1. 사용자 로그인
2. 사용자의 요청에서 cookie를 보고 session id가 존재하는지 판별.
3. 서버에 session이 존재하지 않으므로 생성 및 session id 응답.
  1. 이 때 사용자의 정보는 서버에 저장
4. 사용자는 서버에 요청할 때마다 session id를 전송
5. 서버는 session id를 이용해 사용자 식별
6. 클라이언트 종료 시 session id제거 및 서버에서 session 제거

## 서버에서 로그인 시 쿠키 생성하기

사용자가 로그인에 성공하면 서버에서 쿠키를 생성하여 응답해주어야 한다. 동시성 문제에서 자유롭게 하기 위해 ConcurrentHashMap<String, User>를 사용한다. key는 session id, value는 User이다. 랜덤한 스트링으로 session id를 생성하기 위해 UUID를 이용한다.

```java
public static void makeNewSessionId(User user) {
		// 랜덤한 session id 생성
		String sessionId = UUID.randomUUID().toString();
		sessionIdList.put(sessionId, user);
}
```

이외에도 userId를 이용하여 session id를 알아내는 메소드, session id를 이용하여 userId를 알아내는 메소드, session id를 삭제하는 메소드, hash map에 session id가 존재하는지 체크하는 메소드를 정의하여 필요한 때에 사용할 수 있다.

## 서버에서 로그아웃 시 쿠키 삭제하기

사용자가 로그아웃 하면 더 이상 세션을 유지할 필요가 없다. 서버의 hash map에서 세션을 삭제해주어야 한다.

request로부터 쿠키를 읽어들인 후, session id를 알아낸다. 그 후 SessionHandler의 deleteSession메소드를 호출하여 해당 세션을 삭제한다.
```java
if (path.equals("/logout")) {
// 세션 삭제
String userId = request.getSessionId();
		SessionHandler.deleteSession(userId);
}
```

# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## week 1

### Task 1
    학습 목표

    - HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
    - Java 멀티스레드 프로그래밍을 경험한다.
    - IDE에서 디버깅 도구를 사용하는 방법을 익힌다.
    - 유지보수에 좋은 구조에 대해 고민하고 코드를 개선해 본다.

    기능요구사항
    
    정적인 html 파일 응답
    http://localhost:8080/index.html 로 접속했을 때 src/main/resources/static 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
    HTTP Request 내용 출력
    서버로 들어오는 HTTP Request의 내용을 읽고 적절하게 파싱해서 로거(log.debug)를 이용해 출력한다.

#### 코드 수정사항
- WebServer.java
```java
public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        ExecutorService executor = Executors.newFixedThreadPool(10); // thread 갯수 제한을 위한 thread pool

        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.submit(new RequestHandler(connection)); // 요청이 올 시에 executor queue에 작업 추가
            }
        }
    }
}
```

- RequestHandler.java
```java
public void run() { //RequestHandler의 run 부분
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader dis = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(dis); //request input 받아오기


            logger.info("////// request header start //////");
            String line = br.readLine();
            String[] tokens = line.split(" "); //request의 첫 줄은 요청문 (GET index.html HTTP/1.1)
            while(!line.isEmpty()){ //request header 출력
                logger.info(line);
                line = br.readLine();
            }
            logger.info("////// request header end //////");

            String pathname = "./src/main/resources/static" + tokens[1];
            logger.info("pathname : {}", pathname);
            byte[] body = Files.readAllBytes(new File(pathname).toPath());

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
```
### Task 2

    학습 목표
    
    HTTP Response 에 대해 학습한다.
    MIME 타입에 대해 이해하고 이를 적용할 수 있다.

    기능요구사항
    
    지금까지 구현한 소스 코드는 css 와 파비콘 등을 지원하지 못하고 있다. 다양한 컨텐츠 타입을 지원하도록 개선해 본다.
    지원할 컨텐츠 타입의 확장자 목록
    
    html, css, js, ico, png, jpg

- RequestHandler.java 

switch문을 이용하여 지원하는 content type에 따른 header 추가
지원하지 않는 type의 경우 405 error를 띄우도록 만듬


```java
switch (tokens[1].split("\\.")[1]) { //content type에 따른 response
    case "html":
        response200Header(dos, body.length,"text/html;charset=utf-8");
        responseBody(dos, body);
        break;
    case "css":
        response200Header(dos, body.length,"text/css;charset=utf-8");
        responseBody(dos, body);
        break;
    case "js":
        response200Header(dos, body.length,"text/javascript;charset=utf-8");
        responseBody(dos, body);
        break;
    case "ico":
        response200Header(dos, body.length,"image/x-icon");
        responseBody(dos, body);
        break;
    case "png":
        response200Header(dos, body.length,"image/png");
        responseBody(dos, body);
        break;
    case "jpg":
        response200Header(dos, body.length,"image/jpeg");
        responseBody(dos, body);
        break;
    case "svg":
        response200Header(dos, body.length,"image/svg+xml");
        responseBody(dos,body);
        break;
    default:
        response405Header(dos);
        break;
    }
```

### Task 3

    학습 목표

    HTTP GET 프로토콜을 이해한다.
    HTTP GET에서 parameter를 전달하고 처리하는 방법을 학습한다.
    HTTP 클라이언트에서 전달받은 값을 서버에서 처리하는 방법을 학습한다.

    기능요구사항
    
    GET으로 회원가입 기능 구현

    “회원가입” 메뉴를 클릭하면 http://localhost:8080/register.html 로 이동, 회원가입 폼을 표시한다.
    이 폼을 통해서 회원가입을 할 수 있다.

전반적인 코스 구성 수정 및 기능 추가가 진행됨
#### 구조 변경

```
├── db
│   └── Database.java
├── model
│   └── User.java
└── webserver
    ├── RequestHandler.java
    ├── WebServer.java
    ├── api
    │   ├── ApiFunction.java
    │   ├── ReadFile.java
    │   └── registration
    │       └── Registration.java
    └── http
        ├── HttpRequest.java
        ├── HttpResponse.java
        ├── PathMap.java
        ├── Url.java
        └── enums
            ├── Extension.java
            ├── Methods.java
            └── StatusCode.java
```
**webserver**
- WebServer : webserver 구동기
- RequestHandler : reqeust 입출력 처리
  - **api**
    - ApiFuncion : static file 읽기, 회원가입 등 모든 function의 처리용 interface
    - ReadFile : static file 읽는 용도 class
    - Registration : 회원가입용 class
  - **http**
    - url : url에 대한 path와 parameter의 정보가 담겨있는 class
    - PathMap : 사이트의 모든 경로에 대해서 api function을 맵핑하는 pathtree가 있는 클래스 \
    이 클래스를 이용하여 모든 경로들에 대한 function을 찾을 수 있는
    - HttpRequest : request에 대한 모든 정보가 있는 class
    - HttpResponse : response에 대한 모든 정보가 있는 class
    - **enums**
      - Methods (enum) : methods들에 대한 enum
      - Extention (enum) : 지원하는 모든 확장자들에 대한 content-type이 저장되어있음
      - StatusCode (enum) : response code들에 대한 message가 저장되어있음

    

#### 기능추가
register내부의 index.html파일을 수정하여 회원가입 버튼을 누르면 id, username, password가 url을 통해서 전달되게끔 변경
get 요청이 올 시에 path경로를 분석하여 회원가입 기능을 실행하게끔 변경


## Week2
### Task 4, 5


    학습 목표
    - HTTP POST의 동작방식을 이해하고 이를 이용해 회원가입을 구현할 수 있다.

    - HTTP redirection 기능을 이해하고 회원가입 후 페이지 이동에 적용한다.

    기능요구사항
    - 로그인을 GET에서 POST로 수정 후 정상 동작하도록 구현한다.
    - GET으로 회원가입을 시도할 경우 실패해야 한다.
    - 가입을 완료하면 /index.html 페이지로 이동한다.

#### 코드 수정사항
- 1주차에 피드백받은 다양한 수정사항들 수정완료
- 테스트 코드들 전반적으로 형식에 맞게끔 수정
- request 및 response를 builder pattern을 채용
- pathmap을 트리구조가 아닌 1대1 매칭 형식으로 변경
<br><br>
현재 사용자가 로그인 시, 서버는 사용자에 대한 세션을 생성하고 세션 id를 쿠키값으로 넘겨줌 <br>
로그아웃시에는 해당 유저의 세션을 삭제시키고, 쿠키도 deleted로 변경해주게 됨




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
WebServer.java
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

RequestHandler.java
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



### Task 3
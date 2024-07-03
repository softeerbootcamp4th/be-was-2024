### Java Thread

Java Thread

- `Thread` 로 쓰레드를 생성하는 경우 쓰레드를 직접만든다.
    - 즉 유저가 쓰레드를 관리함

Executors

- Thread만들고 관리하는 것을 고수준의 API Executors에 위임
    - Runnable만 만들고 생성, 종료, 없애기는 Executors에게 위임
- Thread Pool을 만들어 관리
    - Thread 생명 주기를 관리합니다.

개선점

- 위 쓰레드는 OS Thread와 1대 1로 대응하여 실행된다.
    - OS가 Thread를 스케쥴링 한다.
    - JVM이 Thread를 스케쥴링 하는 방법은 없을까?

### HTTP Request 출력

```java
String tmp;
BufferedReader br = new BufferedReader(new InputStreamReader(in));
while((tmp = br.readLine()) && !tmp.empty()) {
	logger.debug(tmp);
}
```

### NIO 라이브러리 사용하지 않고 반환하기

BufferedInputStream

- 다른 `InputStream` 으로 부터 읽는다.
    - `FileInputStream` 은 파일에서 데이터를 읽는다.
- Buffer를 만들어 한번에 보내는 역할
    - `FileInputStream` 은 1byte씩 보낸다.
        - 이는 `FileInputStream.read()` 를 호출할 때 마다 syscall(매우 비쌈)을 호출하는 것이다.

```java
DataOutputStream dos = new DataOutputStream(out);
ByteArrayOutputStream bos = new ByteArrayOutputStream();
FileInputStream fis = new FileInputStream("src/main/resources/static" + request.getPath());
BufferedInputStream bis = new BufferedInputStream(fis);
bis.transferTo(bos);
byte[] body = bos.toByteArray();
```
## 2. 다양한 MimeType 지원하기

### 확장자에서 MimeType으로 변환

1. 우선 경로에서 파일 확장자를 분리한다.

```java
public class PathParser {
    public String fileExtExtract(String fileName) {
        int pointPos = fileName.lastIndexOf(".");
        if(pointPos == -1) {
           throw new IllegalArgumentException("파일 확장자가 존재하지 않는 파일 이름입니다.");
        }
        return fileName.substring(pointPos);
    }
}
```

1. HashMap을 이용하여 파일 이름과 MimeType을 매핑하였다.

```java
public class MimeTypeMapper {
    private Map<String, MimeType> mimeTypeMapper;

    public MimeTypeMapper() {
        mimeTypeMapper = new HashMap<>() {{
            put(".html", MimeType.HTML);
            put(".css", MimeType.CSS);
            put(".js", MimeType.JAVASCRIPT);
            put(".svg", MimeType.SVG);
            put(".ico", MimeType.ICO);
            put(".png", MimeType.PNG);
            put(".jpg", MimeType.JPG);
        }};
    }

    public MimeType getMimeType(String ext) {
        if(mimeTypeMapper.containsKey(ext)) {
            return mimeTypeMapper.get(ext);
        }
        throw new IllegalArgumentException("확장자와 매핑되는 mimeType이 없습니다.");
    }
}
```

1. response의 `content-type` 헤더에 MimeType을 작성해주었다.

```java
dos.writeBytes("Content-Type: " + mimeType + "\r\n");
```

- 구현 방법
    1. Enum을 이용하여 구현하기
        - MimeType을 원시 값으로 나두는 것이 아니라 변수로 가질 수 있어 가독성이 좋다.
        - 모든 MimeType을 순회해야 된다는 단점이 있음
    2. HashMap을 이용하여 구현하기
        - 해싱을 하는 비용이 발생할 수 있음
    3. switch문을 이용하여 구현하기
        - 지원하는 `Content-Type` 이 늘어난다면 코드가 너무 길어질 가능성이 존재

### MimeType

- 미디어 타입이란 문서, 파일 또는 바이트 집합의 성격과 형식
- `type/subtype` 처럼 슬래시로 구분된 두 부분으로 구성됩니다.
    - type: 데이터 타입이 속하는 일반 카테고리
    - subtype: 정확한 종류의 데이터
- `type/subtype;parameter=value` 처럼 매개변수를 추가할 수 있습니다.
    - `charset` : 문자에 사용되는 문자 세트
    - `q` : client가 선호하는 mimeType의 우선순위

### Accept Header

- 클라이언트가 이해 가능한 컨텐츠 타입이 무엇인지를 알려줍니다.
- 콘텐츠 협상을 통해, 서버는 제안 중 하나를 선택하고 사용하며 `Content-Type` 응답 헤더로 클라이언트에게 선택된 타입을 알려줍니다.
    - 콘텐츠 협상
        - 클라이언트에게 적합한 자원을 제공하는 것
        - 클라이언트와 함께 서로의 입장 차이를 줄여나가며 거래할 자원을 결정한다.

### Content-Type

- 리소스의 media type을 나타내기 위해 사용됩니다.
- 반환된 컨텐츠의 컨텐츠 유형이 실제로 무엇인지를 알려줍니다.

### Enum Map vs Hash Map

- HashMap
    - 해싱을 하는 과정이 있으므로 추가적인 연산이 존재
    - 해시 충돌이 일어나는 과정에서 성능이 저하할 수 있다.
- EnumMap
    - 내부적으로 Array를 사용함.
        - enum을 선언한 순서대로 저장
        - oridinal()을 이용하여 순서를 반환 받을 수 있다.
    - get 함수
        - 배열을 통해 직접적으로 접근할 수 있다.
    
    ```java
        public V get(Object key) {
            return (isValidKey(key) ?
                    unmaskNull(vals[((Enum)key).ordinal()]) : null);
        }
    ```

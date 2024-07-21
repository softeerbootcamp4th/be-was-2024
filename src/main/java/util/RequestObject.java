package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;



/**
 * 들어온 요청을 객체화 하여 값을 파싱해서 각 필드별로 저장시키는 클래스
 */
public class RequestObject {

    private static final Logger logger = LoggerFactory.getLogger(RequestObject.class);
    // GET /user/create?name="1234"&password="1" HTTP/1.1 이면

    private final String method;// GET이 들어옴
    private String path;//  /user/create 가 들어옴
    private final String version;//HTTP version

    private byte[] body;

    private int contentLength;

    private final Map<HttpHeader,String> headers = new HashMap<>();


    // 멀티파트 바디 파싱 결과
    private String parsedTitle;
    private String parsedContent;
    private byte[] parsedImage;



    /**
     * RequestObject 생성자
     * @param inputStream Http통신에서 들어온 요청 값 inputStream 값
     */
    public RequestObject(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();

        int c;
        int prev1 = -1, prev2 = -1, prev3 = -1;

        // Read headers
        while ((c = inputStream.read()) != -1) {
            headerBuffer.write(c);
            if (prev3 == '\r' && prev2 == '\n' && prev1 == '\r' && c == '\n') {
                break;
            }
            prev3 = prev2;
            prev2 = prev1;
            prev1 = c;
        }

        logger.debug(headerBuffer.toString("UTF-8"));
        String[] headerLines = headerBuffer.toString("UTF-8").split("\r\n");


        String[] temp = headerLines[0].split("\\s+");
        this.method= temp[0];
        this.path = temp[1];
        this.version= temp[2];

        for(int i=1;i<headerLines.length;i++) {
            String line = headerLines[i];
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(HttpHeader.fromString(key), value);
            }
        }
        String contentLengthHeader = headers.get(HttpHeader.CONTENT_LENGTH);
        contentLength = contentLengthHeader == null ? 0 : Integer.parseInt(contentLengthHeader);
        if(contentLength>0) {
            body = new byte[contentLength];
            int bytesRead = inputStream.read(body,0,contentLength);
            if(bytesRead!=contentLength)
            {
                throw new IOException("Failed to read full request body");
            }
        } else {
            body =null;
        }

        String contentTypeHeader = headers.get(HttpHeader.CONTENT_TYPE);
        if(contentTypeHeader != null && contentTypeHeader.startsWith("multipart/form-data")) {
            try{
                parseMultipart();
            } catch (Exception e)
            {
                e.printStackTrace();
                throw new IOException("Failed to parse multipart body", e);
            }
        }
    }


    /**
     * path Getter
     */
    public String getPath() {
        return this.path;
    }


    /**
     * method Getter
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * version Getter
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * body Getter
     */
    public byte[] getBody() {
        return this.body;
    }

    /**
     * body Setter
     * @param body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * content Getter
     */
    public String getParsedContent() {
        return this.parsedContent;
    }


    /**
     * parsedImage Getter
     */
    public byte[] getParsedImage() {
        return this.parsedImage;
    }

    /**
     * parsedTitle Getter
     */
    public String getParsedTitle(){
        return this.parsedTitle;
    }

    /**
     * Cookies Getter
     */
    public Map<String, String> getCookies() {
        Map<String, String> cookies = new HashMap<>();
        if (headers.containsKey(HttpHeader.COOKIE)) {
            String cookieHeader = headers.get(HttpHeader.COOKIE);
            String[] cookiePairs = cookieHeader.split("; ");
            for (String cookie : cookiePairs) {
                String[] cookieParts = cookie.split("=", 2);
                if (cookieParts.length == 2) {
                    cookies.put(cookieParts[0], cookieParts[1]);
                }
            }
        }
        return cookies;
    }

    private void parseMultipart() throws Exception{
        String contentTypeHeader = headers.get(HttpHeader.CONTENT_TYPE);
        String boundary = extractBoundary(contentTypeHeader);
        if( boundary ==null){
            throw new Exception("Boundary not found error");
        }
        byte[] boundaryBytes =("--" + boundary).getBytes();
        byte[] endBoundaryBytes = ("--" + boundary +"--").getBytes();

        int pos = 0;
        while (pos < body.length){
            int boundaryIndex = indexOf(body, boundaryBytes, pos);
            if (boundaryIndex ==-1) {
                break;
            }
            pos = boundaryIndex + boundaryBytes.length;

            int partEndIndex = indexOf(body,boundaryBytes,pos);
            if(partEndIndex==-1){
                partEndIndex = indexOf(body,endBoundaryBytes,pos);
                if(partEndIndex==-1) {
                    partEndIndex = body.length;
                }
            }

            byte[] part;
            if(partEndIndex> body.length -4){
                part = Arrays.copyOfRange(body, pos, partEndIndex);
            } else {
                part = Arrays.copyOfRange(body,pos,partEndIndex-2);
            }

            try {
                parsePart(part);
            } catch (Exception e) {
                e.printStackTrace(); // 에러 로그 출력
                throw new Exception("Failed to parse part", e);
            }
            pos = partEndIndex;
        }
    }

    private void parsePart(byte[] part) {
        int headerEndIndex = indexOf(part, "\r\n\r\n".getBytes(), 0);
        if (headerEndIndex == -1) {
            headerEndIndex = part.length;
        }

        String headers = new String(part, 0, headerEndIndex);
        byte[] body;

        if (headerEndIndex + 4 > part.length) {
            body = new byte[0]; // 본문이 없는 경우 빈 배열로 설정
        } else {
            body = Arrays.copyOfRange(part, headerEndIndex + 4, part.length);
        }

        if (headers.contains("Content-Disposition: form-data; name=\"content\"")) {
            parsedContent = new String(body);
        } else if (headers.contains("Content-Disposition: form-data; name=\"image\"; filename=\"")) {
            parsedImage = body;
        }
        else if(headers.contains("Content-Disposition: form-data; name=\"title\"")){
            parsedTitle = new String(body);
        }
    }

    private int indexOf(byte[] array, byte[] target, int start) {
        outer: for (int i = start; i <= array.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length());
            }
        }
        return null;
    }
}

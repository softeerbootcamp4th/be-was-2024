package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 요청을 읽어서 객체로 만드는 클래스
 */
public class RequestReader {

    private final String CRLF = "\r\n";
    private final byte[] END_HEADER = {'\r', '\n', '\r', '\n'};
    private final int MAX_REQUEST_LENGTH = 3000;
    private final InputStream inputStream;
    private final Logger logger = LoggerFactory.getLogger(RequestReader.class);

    public RequestReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * 요청을 읽고 객체로 반환하는 메소드
     * @return
     * @throws IOException
     */
    public Request readRequest() throws IOException {

        byte[] bytes = new byte[MAX_REQUEST_LENGTH];
        int length = readRequestHeader(inputStream, bytes);
        String request = new String(bytes).substring(0, length);

        Request httpRequest = Request.parseRequestWithoutBody(request);
        String contentLengthString = httpRequest.getHeadValue("Content-Length");

        Request.Builder requestBuilder = new Request.Builder(httpRequest.getMethod(), httpRequest.getPath())
                .parameter(httpRequest.getParameter())
                .header(httpRequest.getHeader());

        if(contentLengthString!=null){
            int contentLength = Integer.parseInt(contentLengthString);
            byte[] buf = new byte[contentLength];
            readRequestBody(inputStream, buf, contentLength);
            requestBuilder.body(decodeBody(buf));
            StringBuilder sb = new StringBuilder(request);
            sb.append(new String(buf));
            request = sb.toString();
        }

        logger.debug(request);

        return requestBuilder.build();
    }

    private String decodeBody(byte[] body){
        return URLDecoder.decode(new String(body), StandardCharsets.UTF_8);
    }

    private int readRequestHeader(InputStream bufferedInputStream, byte[] buf) throws IOException {
        int count=0;
        int length=0;
        for(int i=0; count<END_HEADER.length&&i<buf.length; i++){
            byte input = (byte) bufferedInputStream.read();
            if(END_HEADER[count++]!=input) count=0;
            buf[i] = input;
            length++;
        }
        return length;
    }

    private void readRequestBody(InputStream bufferedInputStream, byte[] buf, int contentLength) throws IOException {
        bufferedInputStream.read(buf, 0, contentLength);
    }

}

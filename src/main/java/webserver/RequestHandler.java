package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

import chain.core.ChainManager;
import http.HeaderConst;
import http.MyHttpHeaders;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.cookie.MyCookies;
import http.enums.HttpMethodType;
import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;
import http.utils.HttpResponseMessageBuildUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import url.MyURL;
import utils.ByteReadUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ChainManager chainManager;

    public RequestHandler(Socket connectionSocket, ChainManager chainManager) {
        this.connection = connectionSocket;
        this.chainManager = chainManager;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            MyHttpRequest request = buildRequest(in);
            MyHttpResponse response = new MyHttpResponse(request.getVersion());
            chainManager.execute(request, response);

            DataOutputStream dos = new DataOutputStream(out);

            byte[] responseMessage = HttpResponseMessageBuildUtil.build(response);
            sendResponse(dos, responseMessage);
            logger.debug("send response");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private MyHttpRequest buildRequest(InputStream in) throws IOException {
        /** request parsing phase **/
        // 맨 처음 요청 라인 읽기
        String requestLine = ByteReadUtil.readLineToString(in);
        String[] reqLineItems = HttpParseUtil.parseRequestLine(requestLine);
        // 요청 라인 데이터 추출
        HttpMethodType method = HttpMethodTypeUtil.getHttpMethodType(reqLineItems[0]);
        MyURL url = new MyURL(reqLineItems[1]);
        String version = reqLineItems[2];

//        logger.debug("reqline = {}", requestLine);

        // header 목록 읽기
        List<String> headerLines = new ArrayList<>();
        String headerLine;
        while (!(headerLine = ByteReadUtil.readLineToString(in)).isEmpty()) {
            headerLines.add(headerLine);
        }
//        logger.debug("headers = {}", headerLines);

        // 헤더 객체 생성
        MyHttpHeaders headers = new MyHttpHeaders();
        headers.putHeaders(headerLines);

        // 쿠키 파싱
        String cookieLine = headers.getHeader(HeaderConst.Cookie);
        MyCookies cookies = HttpParseUtil.parseCookies(cookieLine);

        byte[] body = in.readNBytes(headers.getContentLength());

        return new MyHttpRequest(method, url, version, headers, cookies, body);
    }

    private void sendResponse(DataOutputStream dos, byte[] responseMessage) {
        try {
            dos.write(responseMessage, 0, responseMessage.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

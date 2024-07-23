package webserver;

import data.HttpRequestMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청을 파싱
 */
public class HttpRequestParser {
    /**
     * inputStream으로 HttpRequestMessage를 만든다
     * @param in
     * @return HttpRequestMessage
     * @throws IOException
     */
    public static HttpRequestMessage getHttpRequestMessage(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(in);
        String tempString;
        while (!(tempString = readLine(bis)).isEmpty()) {
            stringBuilder.append(tempString);
            stringBuilder.append("\n");
        }

        HttpRequestMessage httpRequestMessage = getHttpRequestMessage(stringBuilder.toString());
        Map<String, String> headers = httpRequestMessage.getHeaders();
        String length = headers.get("Content-Length");
        if (length != null) {
            int totalLength = Integer.parseInt(length);
            int readLength = 0;
            byte[] bytes = new byte[Integer.parseInt(length)];
            while(readLength < totalLength) {
                readLength += bis.read(bytes,readLength, totalLength - readLength);
            }
            httpRequestMessage.setBody(bytes);
        }
        return httpRequestMessage;
    }

    private static String readLine(BufferedInputStream bis) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = bis.read()) != -1) {
            if (ch == '\r'){
                bis.read();
                break;
            }
            byteArrayOutputStream.write(ch);
        }
        return byteArrayOutputStream.toString();
    }

    private static HttpRequestMessage getHttpRequestMessage(String requestMessage){
        Map<String, String> queryParams = new HashMap<>();
        String[] headerSplit = requestMessage.split("\n",2);
        String startLine = headerSplit[0];

        String[] startLineSplit = startLine.trim().split("\\s+");
        String method = startLineSplit[0];
        String uri = startLineSplit[1];
        String version = startLineSplit[2];
        if (uri.contains("?")) {
            uri = processQuery(uri, queryParams);
        }
        String[] headerArray =  headerSplit[1].split("\n");
        Map<String, String> headers = getHeaderMap(headerArray);
        Map<String, String> cookies = getCookie(headers.get("Cookie"));
        return new HttpRequestMessage(method,uri,version,queryParams,headers, null,cookies);
    }

    private static Map<String, String> getHeaderMap(String[] headerArray) {
        Map<String, String> headers = new HashMap<>();
        for (String header : headerArray) {
            String[] headerParts = header.split(":", 2);
            headers.put(headerParts[0].strip(), headerParts[1].strip());
        }
        return headers;
    }

    private static Map<String,String> getCookie(String cookieString){
        HashMap<String, String> cookies = new HashMap<>();
        if (cookieString == null) return cookies;
        String[] cookieArray = cookieString.split(";");
        for (String cookie : cookieArray) {
            String[] cookieEntry = cookie.split("=");
            cookies.put(cookieEntry[0].strip(), cookieEntry[1].strip());
        }
        return cookies;
    }

    private static String processQuery(String uri, Map<String, String> queryParams) {
        String queryString = uri.substring(uri.indexOf("?") + 1);
        String[] queries = queryString.split("&");
        for (String query : queries) {
            String[] entry = query.split("=");
            queryParams.put(entry[0], entry[1]);
        }
        uri = uri.substring(0, uri.indexOf("?"));
        return uri;
    }
}

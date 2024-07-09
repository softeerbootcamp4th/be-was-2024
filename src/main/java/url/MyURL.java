package url;

import url.exception.IllegalUrlException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 필요하면 나머지도 구현

public class MyURL {
    private String protocol;
    private String domain;
    private Integer port;
    private String pathname;
    private Map<String, String> parameters;

    public MyURL(String url) {
        String[] urlParts = getUrlEachPart(url);
        protocol = urlParts[0];
        domain = urlParts[1];
        port = urlParts[2] != null ? Integer.parseInt(urlParts[2]) : null;
        pathname = urlParts[3];
        parameters = parseParameter(urlParts[4]);
    }

    public MyURL() {}

    /**
     * 입력받은 문자열의 각 파트( protocol, domain, port, pathname, parameters )를 분리한다. anchor의 경우 서버에 전달되지 않는다.
     * @param url url 문자열
     * @return 각 파트를 담은 String[5]. 파트가 없는 자리는 null
     */
    public static String[] getUrlEachPart(String url) {
        String protocol = null;
        String domain = null;
        String port = null;
        String pathname = null;
        String parameters = null;

        // 프로토콜 검사
        String[] arrWithProtocol = url.split("://", 2);

        // 프로토콜 이후의 파츠
        String afterProtocol;
        if (arrWithProtocol.length == 2) {
            protocol = arrWithProtocol[0];
            afterProtocol = arrWithProtocol[1];
        } else if(arrWithProtocol.length == 1) { // 프로토콜 파트가 없는 경우
            afterProtocol = url;
        } else throw new IllegalUrlException("illegal url");

        //
        int offset = afterProtocol.indexOf("/");

        // domain 부분이 있다면, pathname부분과 domain 부분을 구분
        String afterDomain;
        if (offset > 0) {
            afterDomain = afterProtocol.substring(offset);
            // domain - port 나누기

            String domainWithPort = afterProtocol.substring(0, offset);
            String[] domainAndPortArr = domainWithPort.split(":");

            domain = domainAndPortArr[0];
            if(domainAndPortArr.length == 2) port = domainAndPortArr[1];
        } else {
            afterDomain = afterProtocol;
        }

        String[] pathnameAndParamsArr = afterDomain.split("\\?");
        pathname = pathnameAndParamsArr[0];
        if(pathnameAndParamsArr.length == 2) parameters = pathnameAndParamsArr[1];

        return new String[] {protocol, domain, port, pathname, parameters};
    }

    /**
     * 파라미터 목록을 Map 자료구조로 파싱한다.
     * @param parameterString 파라미터 목록이 포함되어 있는 문자열
     * @return 파라미터들을 파싱한 Map
     */
    public static Map<String, String> parseParameter(String parameterString) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (parameterString == null || parameterString.isEmpty()) return parameters;

        String[] paramLines = parameterString.split("&");
        for (String paramLine : paramLines) {
            // value 에 2개 나올 수 있음
            String[] keyValue = paramLine.split("=", 2);

            if(keyValue.length != 2) continue;

            // key 값만 지정된 것은 전달되지 않은 것과 동일하다고 판단, 파라미터에 추가하지 않는다.
            // 둘을 구분해야 한다면, 이 부분을 변경한다.
            if(keyValue[1].isEmpty()) continue;

            String key = keyValue[0];
            String value = keyValue[1];
            parameters.put(key, value);
        }

        return parameters;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDomain() {
        return domain;
    }

    public Integer getPort() {
        return port;
    }

    public String getPathname() {
        return pathname;
    }


    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}

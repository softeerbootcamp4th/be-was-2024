package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestParser {

    public Request getRequest(InputStream rawHttpRequest) throws IOException {
        Request request = new Request();
        BufferedReader br = new BufferedReader(new InputStreamReader(rawHttpRequest));
        requestLineParse(request, br.readLine());
        requestHeaderParse(request, br);
        return request;
    }

    private void requestHeaderParse(Request request, BufferedReader br) throws IOException {
        String tmp;
        Map<String, String> headers = new ConcurrentHashMap<>();
        while((tmp = br.readLine()) != null && !tmp.isEmpty()) {
            String[] headerKeyValue = tmp.split(": ");
            if(headerKeyValue.length != 2) {
               throw new IllegalStateException("유효하지 않은 Http Header 형식");
            }
            headers.put(headerKeyValue[0], headerKeyValue[1]);
        }
    }

    private void requestLineParse(Request request, String requestLine) {
        String[] requestLineSplit = requestLine.split(" ");
        request.setMethod(requestLineSplit[0]);
        pathParse(request, requestLineSplit[1]);
        request.setHttpVersion(requestLineSplit[2]);
    }

    private void pathParse(Request request, String path) {
        // api path 파싱
        String[] pathSplit = path.split("\\?");
        String apiPath = pathSplit[0];
        request.setPath(apiPath);

        if(pathSplit.length == 1) return;

        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        String rawQueryParameter = pathSplit[1];
        String[] rawQueryParameters = rawQueryParameter.split("\\&");
        for(String queryParameter: rawQueryParameters) {
            String[] queryKeyValue = queryParameter.split("\\=");
            queryParameters.put(queryKeyValue[0], queryKeyValue[1]);
        }
        request.setParameters(queryParameters);
    }

}

package webserver.front.data;

import java.util.HashMap;

public class HttpResponse extends HttpMessage{
    private final HashMap<String, String> additionalHeader = new HashMap<>();
    public String statusCode;
    public String statusText;
    private String firstLine;
    public HttpResponse(String httpVersion, String statusCode, String statusText, byte[] body,String contentType) {
        super(httpVersion,body,contentType);
        this.statusCode = statusCode;
        this.statusText = statusText;
        makeFirstLine();
    }
    private void makeFirstLine() {
        this.firstLine = this.getHttpVersion()+" " +statusCode+" "+ statusText;
    }
    public void addLocation(String location){
        additionalHeader.put("Location", location);
    }
    public HashMap<String, String> getAdditionalHeader() {
        return additionalHeader;
    }
    public String getFirstLine() {
        return firstLine;
    }


}


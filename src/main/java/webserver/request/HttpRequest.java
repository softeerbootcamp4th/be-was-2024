package webserver.request;

import java.io.IOException;

public class HttpRequest {

    private Method method;
    private Path path;
    private Header header;

    public HttpRequest(String request) throws IOException {
        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");
        this.method = new Method(startLine[0]);
        this.path = new Path(startLine[1]);
        this.header = new Header(request);
    }

    public Path getPath(){
        return this.path;
    }

}

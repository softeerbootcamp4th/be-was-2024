package util;

import handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestObject {

    private static final Logger logger = LoggerFactory.getLogger(RequestObject.class);
    // GET /user/create?name="1234"&password="1" HTTP/1.1 이면

    private final String method;// GET이 들어옴
    private String path;//  /user/create 가 들어옴
    private final String version;//HTTP version

    private byte[] body;

    private int contentLength;

    public RequestObject(InputStream inputStream) throws IOException
    {
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


        this.method=headerLines[0].split(" ")[0];
        this.path=headerLines[0].split(" ")[1];
        this.version=headerLines[0].split(" ")[2];


        for(int i=1;i<headerLines.length;i++)
        {
            if(headerLines[i].startsWith("Content-Length"))
            {
                this.contentLength=Integer.parseInt(headerLines[i].split(":")[1].trim());
                break;
            }
        }


        if(contentLength>0)
        {
            body = new byte[contentLength];
            int bytesRead = inputStream.read(body,0,contentLength);
            if(bytesRead!=contentLength)
            {
                throw new IOException("Failed to read full request body");
            }
        }
        else
        {
            body =null;
        }



    }
    public String getPath()
    {
        return this.path;
    }

    public String getMethod()
    {
        return this.method;
    }

    public String getVersion()
    {
        return this.version;
    }
    public byte[] getBody()
    {
        return this.body;
    }
    public void setBody(byte[] body)
    {
        this.body = body;
    }
}

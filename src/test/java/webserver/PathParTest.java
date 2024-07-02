package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathParTest
{
    private final String url = "GET /index.html HTTP/1.1";
    private PathPar pathPar = new PathPar();

    @DisplayName("url테스트")
    @Test
    public void url테스트()
    {
        Assertions.assertEquals("/index.html",pathPar.getUrl(url));
    }

    @DisplayName("method테스트")
    @Test
    public void method테스트()
    {
        Assertions.assertEquals("GET",pathPar.getHttpMethod(url));
    }
}
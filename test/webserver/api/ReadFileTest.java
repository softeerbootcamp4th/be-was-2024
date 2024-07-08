package webserver.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileTest {
    ReadFile readFile;
    HttpRequest request;

    @BeforeEach
    void setUp() {
        readFile = new ReadFile();
    }


    @Test
    void readFile() {
        String fileroute = "/index.html";
        request = new HttpRequest.ReqeustBuilder("GET " + fileroute + " HTTP/1.1").build();
        HttpResponse response = readFile.funcion(request);
        assertNotNull(response.getBody());
    }



}
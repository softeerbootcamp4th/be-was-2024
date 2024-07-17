package webserver.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileTest {
    ReadFileHandler readFile;
    HttpRequest request;

    @BeforeEach
    void setUp() {
        readFile = new ReadFileHandler();
    }

    @DisplayName("check readfile")
    @Test
    void readFile() throws IOException {
        //given
        String fileroute = "/global.css";
        request = new HttpRequest.RequestBuilder("GET " + fileroute + " HTTP/1.1").build();

        //when
        HttpResponse response = readFile.function(request);

        //then
        assertNotNull(response.getBody());
    }



}
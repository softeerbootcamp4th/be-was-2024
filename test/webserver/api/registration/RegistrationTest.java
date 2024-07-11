package webserver.api.registration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    @DisplayName("check registration")
    @Test
    void register() throws IOException {
        //given
        FunctionHandler register = new Registration();
        HttpRequest request = new HttpRequest.ReqeustBuilder("POST /registration HTTP/1.1")
                .addHeader("Content-Length", "34")
                .setBody("id=2&username=1&email=3&password=4".getBytes())
                .build();

        //when
        HttpResponse response  = register.function(request);

        //then
        assertEquals(StatusCode.CODE302, response.getStatusCode());

    }



}
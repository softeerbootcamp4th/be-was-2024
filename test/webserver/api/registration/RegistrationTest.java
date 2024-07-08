package webserver.api.registration;

import org.junit.jupiter.api.Test;
import webserver.api.ApiFunction;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    @Test
    void register() {
        ApiFunction register = new Registration();
        HttpRequest request = new HttpRequest.ReqeustBuilder("POST /registration HTTP/1.1")
                .addHeader("Content-Length", "34")
                .setBody("id=2&username=1&email=3&password=4")
                .build();
        HttpResponse response  = register.function(request);
        System.out.println(response.getHeader());
    }

}
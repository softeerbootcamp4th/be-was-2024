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
        HttpRequest request = new HttpRequest("GET /registration?id=1&username=3&password=5 HTTP/1.1");
        HttpResponse response  = register.funcion(request);
        System.out.println(response.getHeader());
    }

}
package webserver.api;

import db.Database;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    @Test
    void funcion() {
        String userid = "asdfasdf";
        HttpRequest request = new HttpRequest("GET /registration?id="+userid+"&username=2&password=3 HTTP/1.1");
        new Registration().funcion(request);
        assertNotNull(Database.findUserById(userid));
    }
}
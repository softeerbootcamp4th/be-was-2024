package util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
class UtilsTest {

    @Test
    void getAllStrings() throws IOException {

        //given
        String input = "Hello, World!\nThis is a test.\nJava is fun.\n";

        byte[] inputBytes = input.getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        //when
        String allStrings = Utils.getAllStrings(inputStream);

        //then
        assertEquals("Hello, World!\nThis is a test.\nJava is fun.", allStrings);

    }

    @Test
    void contentType() {

        //given
        String contentType = "text/html";

        //when
        String result = Utils.getContentType("html");

        //then
        assertEquals(contentType, result);

    }

}
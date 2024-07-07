package util;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
class UtilsTest {

    @Test
    void getAllStrings() throws IOException {

        //given
        String input = "POST /api/resource HTTP/1.1\n" +
                "Host: www.example.com\n" +
                "Content-Type: application/json\n" +
                "Content-Length: 58\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Accept-Language: en-US,en;q=0.9\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "\n" +
                "{\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"email\": \"john.doe@example.com\"\n" +
                "}\r\n\r\n";

        byte[] inputBytes = input.getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        //when
        String allStrings = Utils.getAllStrings(inputStream);

        //then
        assertEquals("POST /api/resource HTTP/1.1\n" +
                "Host: www.example.com\n" +
                "Content-Type: application/json\n" +
                "Content-Length: 58\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Accept-Language: en-US,en;q=0.9\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "\n" +
                "{\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"email\": \"john.doe@example.com\"\n" +
                "}\r\n\r\n", allStrings);

    }

}
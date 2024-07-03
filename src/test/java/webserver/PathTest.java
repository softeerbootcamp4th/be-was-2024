package webserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void getExtension() {

        //given
        Path path = new Path("/index.html");
        String extension = "html";

        //when
        String result = path.getExtension();

        //then
        assertEquals(extension, result);

    }


}
package webserver;

import exception.NotExistException;
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

    @Test
    void getExtensionNoneExtensionCase(){

        //given
        Path path = new Path("/create");

        //then
        assertThrows(NotExistException.class, path::getExtension);

    }

    @Test
    void isStaticTrue(){

        //given
        Path path = new Path("/index.html");

        //when
        boolean isStatic = path.isStatic();

        //then
        assertTrue(isStatic);

    }

    @Test
    void isStaticFalse(){

        //given
        Path path = new Path("/create");

        //when
        boolean isStatic = path.isStatic();

        //then
        assertFalse(isStatic);

    }


}
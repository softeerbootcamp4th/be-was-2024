package webserver.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.enums.Extension;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionTest {
    @DisplayName("normal extention test")
    @Test
    void extensionCssTest() {
        //given
        String label = "css";

        //when
        Extension extension = Extension.valueOfExtension(label);

        //then
        assertNotEquals(extension.getContentType(),label);
    }


    @DisplayName("not supported extention")
    @Test
    void extensionExceptionTest() {
        //given
        String label = "asdf";

        //when
        Extension extension = Extension.valueOfExtension(label);

        //then
        assertNotEquals(extension.getContentType(),"");
    }

}
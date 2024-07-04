package webserver.extension;

import org.junit.jupiter.api.Test;
import webserver.http.enums.Extension;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionTest {
    @Test
    void extensionCssTest() {
        String label = "css";
        Extension extension = Extension.valueOfExtension(label);
        assertNotEquals(extension.getContentType(),label);
    }

    @Test
    void extensionExceptionTest() {
        String label = "asdf";
        Extension extension = Extension.valueOfExtension(label);
        assertNotEquals(extension.getContentType(),"");
    }

}
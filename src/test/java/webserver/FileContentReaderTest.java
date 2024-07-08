package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileContentReaderTest {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();


    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html"})
    @DisplayName("파일 읽기 성공")
    void FileReaderTestSuccess(String uri) throws IOException {
        // When
        byte[] content = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNotEquals(content, null);
        //temp 변경
    }

    @ParameterizedTest
    @ValueSource(strings = "/hello")
    @DisplayName("파일 읽기 실패")
    void FileReaderTestNotFound(String uri) throws IOException {
        // Given
        byte[] message = "<h1>404 NOT FOUND</h1>".getBytes();

        // When
        byte[] content = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNotEquals(content, message);
    }

    @Test
    @DisplayName("String, Byte 비교")
    void StringByteCompare() {
        File file = new File("src/main/resources/static/favicon.ico");
        System.out.println("file = " + file);

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: ");
        }
        System.out.println("contentBuilder = " + contentBuilder);

        String temp2 = contentBuilder.toString();
        byte[] temp1 = temp2.getBytes();

        FileInputStream fis = null;
        byte[] byteArray = null;

        try {
            fis = new FileInputStream(file);
            byteArray = new byte[(int) file.length()];

            int bytesRead = 0;
            int offset = 0;
            while (offset < byteArray.length
                    && (bytesRead = fis.read(byteArray, offset, byteArray.length - offset)) >= 0) {
                offset += bytesRead;
            }

            if (offset < byteArray.length) {
                throw new IOException("Could not completely read the file " + file.getName());
            }

            System.out.println("byteArray = " + byteArray);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int tn = 0;
        int tp = 0;
        int tz = 0;
        int tten = 0;
        int m1 = 0;
        int m3 = 0;
        for (byte b : temp1) {
            if (b > 0) {
                tp++;
                if (b == 10 || b == 13) {
                    tten++;
                }
            } else if (b < 0) {
                tn++;
                if (b == -3) {
                    m3++;
                } else if (b == -1) {
                    m1++;
                }
            } else {
                tz++;
            }
        }
        System.out.println("p = " + tp);
        System.out.println("n = " + tn);
        System.out.println("z = " + tz);
        System.out.println("ten = " + tten);
        System.out.println("m3 = " + m3);
        System.out.println("m1 = " + m1);

        System.out.println("(p + n + z) = " + (tp + tn + tz));

        int n = 0;
        int p = 0;
        int z = 0;
        int ten = 0;

        for (byte b : byteArray) {
            if (b > 0) {
                p++;
                if (b == 10 || b == 13) {
                    ten++;
                }
            } else if (b < 0) {
                n++;

            } else {
                z++;
            }
        }
        System.out.println("p = " + p);
        System.out.println("n = " + n);
        System.out.println("z = " + z);
        System.out.println("ten = " + ten);


        System.out.println("(p + n + z) = " + (p + n + z));

        System.out.println("adsaa");
    }

    @Test
    @DisplayName("aa")
    void ad() {
        String str = "string to bytes";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_16);
        System.out.println("str: " + str);

        String convertedStr = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("converted str: " + convertedStr);
    }

    @Test
    void ByteTest() {
        byte[] bytes = {65, 66, -49, -128, 1, -1};
        String convertedStr = new String(bytes);

        System.out.println("converted str: " + convertedStr);
        byte[] getByte = convertedStr.getBytes();
        System.out.println(Arrays.toString(convertedStr.getBytes()));

    }

    @Test
    @DisplayName("aa")
    void ad2() {
//        byte[] bytes = {-119, 80, 78, 71, 13, 10, 26, 10};
//        byte[] bytes = {1, -1, 1, -1, -2};
//        byte[] bytes = {1, 2, 3, 10, 20, 30, 60, 100, 111};
//        byte[] bytes = {0, 1};
//        byte[] bytes = {0, -1};
//        byte[] bytes = {80, 78, 71, 13, 10, 26, 10};


        // 194 - 256 = 62
//        byte[] bytes = {65, -62, -127, 66};
//        byte[] bytes = {65, (byte) 0xC2, (byte) 0xA1, 66};

        //        byte[] bytes = {1, 2, -49, -128, 1, 2};
//        byte[] bytes = {(byte) 0xCF, (byte) 0x80};

        // 결과
        // 1100 0000 .. 0000 0011
        // ->
        //

//        byte[] bytes = {-49, -127};
        // 1100 1111  .. 1000 0001
        // 0000 0011 1100 0001

//        byte[] bytes = {-17, -65, -67};

        byte[] bytes = {1, -124, -123, -122, -121};

        String convertedStr = new String(bytes);
        String convertedStr1 = new String(bytes, StandardCharsets.UTF_16);


        ByteBuffer buffer = StandardCharsets.UTF_8.encode(convertedStr);
        String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();

        System.out.println("converted str: " + convertedStr);

        System.out.println(convertedStr.getBytes());
        System.out.println(Arrays.toString(convertedStr.getBytes()));

    }

    @Test
    @DisplayName("aa")
    void ad3() {
        byte[] bytes = {80, 78, 71, 13, 10, 26, 10, -98, -2, -44};

        String convertedStr = new String(bytes, StandardCharsets.ISO_8859_1);
        System.out.println("converted str: " + convertedStr);
    }

    @Test
    @DisplayName("Decoder 확인")
    void charsetDecode() {
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        Charset charset = decoder.charset();
        String defaultCharset = charset.name();

        System.out.println("Default Charset: " + defaultCharset);
    }


    @Test
    @DisplayName("BE, LE 확인")
    void isBE() {
        ByteOrder order = ByteOrder.nativeOrder();

        if (order.equals(ByteOrder.BIG_ENDIAN)) {
            System.out.println("System is Big Endian");
        } else if (order.equals(ByteOrder.LITTLE_ENDIAN)) {
            System.out.println("System is Little Endian");
        } else {
            System.out.println("Unknown Endian");
        }
    }

}

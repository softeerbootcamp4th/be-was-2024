package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringParserTest {

    private HttpRequest httpRequest;
    private final String boundary = "boundary";

    @BeforeEach
    public void setUp() {
        httpRequest = new HttpRequest();
    }

    static Stream<Arguments> multipartDataProvider() {
        return Stream.of(
                Arguments.of(
                        "--boundary\r\n" +
                                "Content-Disposition: form-data; name=\"username\"\r\n" +
                                "\r\n" +
                                "testuser\r\n" +
                                "--boundary--",
                        Map.of("username", "testuser"),
                        null,
                        null
                ),
                Arguments.of(
                        "--boundary\r\n" +
                                "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n" +
                                "\r\n" +
                                "This is a test file content.\r\n" +
                                "--boundary--",
                        Map.of(),
                        "test.txt",
                        "This is a test file content."
                ),
                Arguments.of(
                        "--boundary\r\n" +
                                "Content-Disposition: form-data; name=\"username\"\r\n" +
                                "\r\n" +
                                "testuser\r\n" +
                                "--boundary\r\n" +
                                "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n" +
                                "\r\n" +
                                "This is a test file content.\r\n" +
                                "--boundary--",
                        Map.of("username", "testuser"),
                        "test.txt",
                        "This is a test file content."
                )
        );
    }

    @DisplayName("processMultipartData: multipart/form-data 형식의 데이터를 파싱한다.")
    @ParameterizedTest(name = "{index} => body={0}, expectedFormData={1}, expectedFileName={2}, expectedFileContent={3}")
    @MethodSource("multipartDataProvider")
    void processMultipartData(String body, Map<String, String> expectedFormData, String expectedFileName, String expectedFileContent) {
        httpRequest.putBody(body.getBytes(StandardCharsets.ISO_8859_1));
        StringParser.processMultipartData(httpRequest, boundary);

        Map<String, String> formData = httpRequest.getFormData();
        Map<String, byte[]> fileData = httpRequest.getFileData();

        assertThat(formData).isEqualTo(expectedFormData);

        if (expectedFileName != null) {
            assertThat(fileData).containsKey(expectedFileName);
            assertThat(new String(fileData.get(expectedFileName), StandardCharsets.ISO_8859_1).trim())
                    .isEqualTo(expectedFileContent);
        } else {
            assertThat(fileData).isEmpty();
        }
    }

    @DisplayName("parseSessionId: 세션 쿠키에서 세션 아이디를 추출한다.")
    @ParameterizedTest(name = "Test {index} => cookie={0}, expected={1}")
    @CsvSource({
            "sid=1234; userId=1234, 1234",
            "sid=abcd; userId=1234; name=1234, abcd",
            "sid=5678; userId=abcd; name=1234; email=1234, 5678",
            "userId=1234; sid=efgh; name=abcd, efgh",
            "name=abcd; sid=ijkl; email=1234, ijkl",
            "userId=abcd; name=1234; sid=mnop, mnop",
            "sid=qrst, qrst",
            "sid=uvwx; sid=1234, uvwx",
            "userId=abcd; name=1234; sid=5678; email=1234; sid=91011, 5678"
    })
    void parseSessionId(String cookie, String expected) {
        // when
        String sessionId = StringParser.parseSessionId(cookie).orElse(null);

        // then
        assertThat(sessionId).isEqualTo(expected);
    }

    @DisplayName("parseSessionId: 세션 쿠키가 sid를 포함하지 않으면 빈 Optional을 반환한다.")
    @ParameterizedTest(name = "Test {index} => cookie={0}")
    @ValueSource(strings = {
            "userId=1234",
            "name=1234",
            "email=1234",
            "userId=1234; name=1234",
            "userId=1234; email=1234",
            "name=1234; email=1234",
            "userId=1234; name=1234; email=1234",
            "",
            " ",
            "   "
    })
    void filterSessionId_no_sid(String cookie) {
        // when
        Optional<String> sessionId = StringParser.parseSessionId(cookie);

        // then
        assertThat(sessionId).isEmpty();
    }

    @DisplayName("parseSessionId: 세션 쿠키가 null이면 빈 Optional을 반환한다.")
    @Test
    void filterSessionId_null() {
        // when
        Optional<String> sessionId = StringParser.parseSessionId(null);

        // then
        assertThat(sessionId).isEmpty();
    }
}

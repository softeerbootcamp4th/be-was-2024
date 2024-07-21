package util;

import constant.FileExtensionType;
import constant.HttpMethod;
import constant.MimeType;
import dto.HttpRequest;
import dto.multipart.MultiPartData;
import dto.multipart.MultiPartDataOfFile;
import dto.multipart.MultiPartDataOfText;
import exception.InvalidHttpRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HttpRequestParser 테스트")
public class HttpRequestParserTest {
    private static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String USER_ID = "javajigi";
    private static final String PASSWORD = "password";
    private static final String NAME = "박재성";
    private static final String ENCODED_NAME = "%EB%B0%95%EC%9E%AC%EC%84%B1";
    private static final String ENCODED_EMAIL = "javajigi%40slipp.net";
    private static final String EMAIL = "javajigi@slipp.net";



    @Test
    @DisplayName("post 요청 파싱 성공")
    public void ParsePostRequestSuccess() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream((
                HttpMethod.POST.name() + " /user/create "+ HTTP_VERSION + CRLF +
                "Content-Type: " + MimeType.APPLICATION_FORM_URLENCODED.getTypeName() + CRLF +
                "Content-Length: "+ 93 + CRLF + CRLF +
                "userId=" + USER_ID +
                "&password=" + PASSWORD +
                "&name=" + ENCODED_NAME +
                "&email=" + ENCODED_EMAIL + CRLF
                ).getBytes());

        BufferedInputStream bis = new BufferedInputStream(inputStream);
        // when
        HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(bis);
        Map<String, String> parsedRequestBody = HttpRequestParser.parseUrlEncodedFormData(httpRequest);

        // then
        assertThat(httpRequest.getHttpMethod()).isEqualTo(HttpMethod.POST);

        assertThat(httpRequest.getPath().get()).isEqualTo("/user/create");

        assertThat(httpRequest.getHeader("Content-Type").get())
                .contains(MimeType.APPLICATION_FORM_URLENCODED.getTypeName());

        assertThat(parsedRequestBody).containsEntry("userId", USER_ID)
                .containsEntry("password", PASSWORD)
                .containsEntry("name", NAME)
                .containsEntry("email", EMAIL);
    }

    @Test
    @DisplayName("정적 파일 요청 파싱 성공")
    public void ParseGetRequestSuccess() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream((
                HttpMethod.GET.name() + " /register/index.html " + HTTP_VERSION + CRLF + CRLF).getBytes());
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        // when
        HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(bis);

        // then
        assertThat(httpRequest.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath().get()).isEqualTo("/register/index.html");
        assertThat(httpRequest.getExtensionType().get()).isEqualTo("html");
    }

    @Test
    @DisplayName("잘못된 HttpMethod 파싱 실패")
    public void ParseWrongHttpMethodFail() {
        // given
        // 잘못된 HttpMethod
        InputStream inputStream = new ByteArrayInputStream((
                "GOOD /register/index.html " + HTTP_VERSION + CRLF + CRLF).getBytes());
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        // when & then
        assertThatThrownBy(() -> HttpRequestParser.parseHttpRequest(bis))
                .isInstanceOf(InvalidHttpRequestException.class)
                .hasMessage("Incorrect HttpMethod");

    }

    @Test
    @DisplayName("multipart 요청 파싱 성공")
    public void ParseMultipartSuccess() {
        // given
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeader("Content-Type", MimeType.MULTIPART_FORM_DATA.getTypeName());
        httpRequest.setHeader("Content-Type", "boundary=Boundary");
        String bodyText = "--Boundary\r\n" +
                "Content-Disposition: form-data; name=text_field\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "This is a text field value.\r\n" +
                "--Boundary\r\n" +
                "Content-Disposition: form-data; name=image_file; filename=example.jpg\r\n" +
                "Content-Type: image/jpeg\r\n" +
                "\r\n" +
                "FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707\r\n" +
                "--Boundary--\r\n";
        byte[] body = bodyText.getBytes(StandardCharsets.ISO_8859_1);
        httpRequest.setBody(body);

        // when
        Map<String, MultiPartData> multipartFormData = HttpRequestParser.parseMultipartFormData(httpRequest);
        MultiPartDataOfText multiPartDataOfText = (MultiPartDataOfText) multipartFormData.get("text_field");
        MultiPartDataOfFile multiPartDataOfFile = (MultiPartDataOfFile) multipartFormData.get("image_file");

        // then
        assertThat(multipartFormData).hasSize(2);

        assertThat(multiPartDataOfText.getText()).isEqualTo("This is a text field value.");
        assertThat(multiPartDataOfText.getContentType()).isEqualTo(FileExtensionType.PLAIN);

        assertThat(multiPartDataOfFile.getFileName()).isEqualTo("example.jpg");
        assertThat(multiPartDataOfFile.getContent())
                .isEqualTo("FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707"
                        .getBytes(StandardCharsets.ISO_8859_1));
        assertThat(multiPartDataOfFile.getContentType()).isEqualTo(FileExtensionType.JPG);
    }
}

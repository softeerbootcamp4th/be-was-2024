import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    @DisplayName("GET /index.html 200 SUCCESS")
    void getIndexHtmlTest() throws IOException {
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when
        // index.html 요청 및 HttpStatus, ContentType 검증
        Response response = RestAssured
                .given().log().all()
                .when().get("/index.html")
                .then().log().all()
                    // Http Status 및 Content Type 검증
                    .assertThat()
                        .statusCode(200)
                        .contentType("text/html")
                .extract().response();

        // 응답 본문을 문자열로 읽기
        String responseBody = response.getBody().asString();

        // 예상되는 파일 내용 읽기
        File file = new File("src/main/resources/static/index.html");
        String expectedContent = new String(Files.readAllBytes(file.toPath()));

        // then
        // index.html 파일 검증
        assertEquals(responseBody, expectedContent);

    }

    @Test
    @DisplayName("GET /register/index.html 200 SUCCESS")
    void getRegisterIndexHtmlTest() throws IOException {
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when
        // registration/index.html 요청 및 HttpStatus, ContentType 검증
        Response response = RestAssured
                .given().log().all()
                .when().get("/register/index.html")
                .then().log().all()
                    // Http Status 및 Content Type 검증
                    .assertThat()
                        .statusCode(200)
                        .contentType("text/html")
                .extract().response();

        // 응답 본문을 문자열로 읽기
        String responseBody = response.getBody().asString();

        // 예상되는 파일 내용 읽기
        File file = new File("src/main/resources/static/register/index.html");
        String expectedContent = new String(Files.readAllBytes(file.toPath()));

        // then
        // index.html 파일 검증
        assertEquals(responseBody, expectedContent);

    }

    @Test
    @DisplayName("GET /register/indox.html 404 NOT FOUND")
    void getRegisterIndexHtmlFail(){
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when & then
        // 404 오류 응답
        RestAssured
                .given().log().all()
                // 잘못된 정적 파일 경로
                .when().get("/register/indox.html")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                .statusCode(404)
                .contentType("text/html");

    }

    @Test
    @DisplayName("GET /user/create 200 SUCCESS")
    void getUserCreateTest() throws IOException {
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when
        // email 정보 누락
        Response response = RestAssured
                .given().log().all()
                    .param("userId","javajigi")
                    .param("password","password")
                    .param("name","%EB%B0%95%EC%9E%AC%EC%84%B1")
                    .param("email","javajigi@gmail.com")
                .when().get("/user/create")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                    .statusCode(200)
                    .contentType("text/html")
                .extract().response();

        // 응답 본문을 문자열로 읽기
        String responseBody = response.getBody().asString();

        // 예상되는 파일 내용 읽기
        File file = new File("src/main/resources/static/login/index.html");
        String expectedContent = new String(Files.readAllBytes(file.toPath()));

        // then
        // login/index.html 파일 검증
        assertEquals(responseBody, expectedContent);
    }

    @Test
    @DisplayName("GET /user/create 404 NOT FOUND")
    void getUserCreateFail() throws IOException {
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when & then
        // email 정보 누락
        // 404 오류 응답
        RestAssured
                .given().log().all()
                .param("userId","javajigi")
                .param("password","password")
                .param("name","%EB%B0%95%EC%9E%AC%EC%84%B1")
                .when().get("/user/create")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                    .statusCode(404)
                    .contentType("text/html");

    }
}

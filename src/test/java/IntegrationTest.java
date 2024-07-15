import handler.HandlerManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    @DisplayName("GET /index.html 200 SUCCESS")
    void getIndexHtmlTest(){
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
        String expectedContent = new String(HandlerManager.readStaticFile("index.html"), StandardCharsets.UTF_8);

        // then
        // index.html 파일 검증
        assertEquals(responseBody, expectedContent);

    }

    @Test
    @DisplayName("GET /register/index.html 200 SUCCESS")
    void getRegisterIndexHtmlTest(){
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
        String expectedContent = new String(Objects.requireNonNull(HandlerManager.readStaticFile("register/index.html")));

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
    @DisplayName("POST /user/create 302 FOUND")
    void postUserCreateTest() {
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when & then
        // 정상 처리시, 302 redirect 응답 반환
        RestAssured
                .given().log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("userId","javajigi")
                    .formParam("password","password")
                    .formParam("name","%EB%B0%95%EC%9E%AC%EC%84%B1")
                    .formParam("email","javajigi@gmail.com")
                .when().post("/user/create")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                    .statusCode(302);
    }

    @Test
    @DisplayName("POST /user/create 404 NOT FOUND (user 정보 누락)")
    void getUserCreateFail1(){
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when & then
        // email 정보 누락
        // 404 오류 응답
        RestAssured
                .given().log().all()
                .contentType("application/x-www-form-urlencoded")
                .formParam("userId","javajigi")
                .formParam("password","password")
                .formParam("name","%EB%B0%95%EC%9E%AC%EC%84%B1")
                .when().get("/user/create")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                    .statusCode(404)
                    .contentType("text/html");
    }

    @Test
    @DisplayName("POST /user/create 404 NOT FOUND (잘못된 Http Method)")
    void getUserCreateFail2(){
        // given
        RestAssured.baseURI = "http://localhost:8080";

        // when & then
        // email 정보 누락
        // 404 오류 응답
        RestAssured
                .given().log().all()
                .contentType("application/x-www-form-urlencoded")
                .formParam("userId","javajigi")
                .formParam("password","password")
                .formParam("name","%EB%B0%95%EC%9E%AC%EC%84%B1")
                .formParam("email","javajigi@gmail.com")
                .when().get("/user/create")
                .then().log().all()
                // Http Status 및 Content Type 검증
                .assertThat()
                .statusCode(404)
                .contentType("text/html");
    }
}

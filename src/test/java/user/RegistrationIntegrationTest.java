package user;

import model.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.common.UserJsonBuilder;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    HttpURLConnection connection;

    @BeforeEach
    public void setUp() throws Exception{
        // 실제 서버에 데이터베이스 초기화 요청
        URL registerUrl = new URL("http://localhost:8080/database/init");
        connection = (HttpURLConnection) registerUrl.openConnection();
        connection.setRequestMethod("GET");
        System.out.println("******** DATABASE INITIALIZED ********");
    }

    /**
     * Mock서버를 사용 불가능하므로 실제 서버를 열어놓고 요청을 보낸다.
     * 테스트 서버와 실제 서버의 Database 객체가 상이하므로 실제 서버에 api 요청을 통해서 실제 서버의 Database를 조회하여 검증하였다.
     */
    @Test
    @DisplayName("유저 회원가입 테스트 (성공 case)")
    public void userRegistrationTestSuccess() throws Exception {
        // 1. 실제 서버에 회원가입 요청
        URL registerUrl = new URL("http://localhost:8080/user/create?userId=testid&name=testname&password=testpassword");
        connection = (HttpURLConnection) registerUrl.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(200, connection.getResponseCode());

        // 2. 실제 서버의 User DB 조회
        URL getUserUrl = new URL("http://localhost:8080/user/list");
        connection = (HttpURLConnection) getUserUrl.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(200, connection.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String responseLine;
        StringBuilder responseContent = new StringBuilder();
        while ((responseLine = in.readLine()) != null) {
            responseContent.append(responseLine);
        }
        String response = responseContent.toString();
        System.out.println("response = " + response);

        // 3. 조회 결과 파싱
        User[] users = UserJsonBuilder.parseJsonToUsers(response);
        Assertions.assertEquals(users.length, 1);

        connection.disconnect();
    }

    @Test
    @DisplayName("유저 회원가입 테스트 (실패 - 중복 회원 Id)")
    public void userRegistrationTestFailOnDuplicate() throws Exception {
        // 1. 실제 서버에 회원가입 요청
        URL registerUrl1 = new URL("http://localhost:8080/user/create?userId=testid1&name=testname1&password=testpassword1");
        connection = (HttpURLConnection) registerUrl1.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(200, connection.getResponseCode());

        URL registerUrl2 = new URL("http://localhost:8080/user/create?userId=testid1&name=testname2&password=testpassword2");
        connection = (HttpURLConnection) registerUrl2.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(200, connection.getResponseCode());

        // 2. 실제 서버의 User DB 조회
        URL getUserUrl = new URL("http://localhost:8080/user/list");
        connection = (HttpURLConnection) getUserUrl.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(200, connection.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String responseLine;
        StringBuilder responseContent = new StringBuilder();
        while ((responseLine = in.readLine()) != null) {
            responseContent.append(responseLine);
        }

        String response = responseContent.toString();
        System.out.println("response = " + response);

        // 3. 조회 결과 파싱
        User[] users = UserJsonBuilder.parseJsonToUsers(response);
        for (User user : users) {
            System.out.println(user.getUserId());
        }

        Assertions.assertEquals(users.length, 1);
        Assertions.assertNotEquals(users.length, 2);

        connection.disconnect();
    }
}

package webserver.api.registration;

import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.PageBuilder;
import webserver.session.SessionDAO;
import webserver.util.ParamsParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 회원가입 기능 class
 */
public class Registration implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new Registration();

        return single_instance;
    }

    /**
     * id 정규표현식
     * <p>
     *     영어로 시작해야 하며, 3~11글자여야 한다.
     * </p>
     */
    static final String idRegex = "^[a-zA-Z][a-zA-Z0-9]{3,11}$";
    /**
     * password 정규표현식
     * <p>
     *     영어 및 숫자로만 이루어져야 하며, 6 ~ 20글자여야 한다.
     * </p>
     */
    static final String passwordRegex = "^[a-zA-Z0-9]{6,20}$";


    /**
     * 회원가입을 진행한다
     * <p>
     *     회원가입시 id, password에 대한 표현식 및 중복 검증을 진행하고 DB에 저장한다
     * </p>
     * <p>
     *     회원가입 완료 시 메인 페이지로 redirect 한다.
     * </p>
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        UserDAO userDAO = new UserDAO();
        String body = URLDecoder.decode(new String(request.getBody()),StandardCharsets.UTF_8) ;


        Map<String, String> params = ParamsParser.parseParams(body);

        String id =params.get("id");
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");

        if(id == null || id.isEmpty()
                || username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || email == null || email.isEmpty()){
            return new HttpResponse.ResponseBuilder(404).build();
        }

        Pattern idpattern = Pattern.compile(idRegex);
        Pattern passwordpattern = Pattern.compile(passwordRegex);
        Matcher idmatcher = idpattern.matcher(id);
        Matcher passwordmatcher = passwordpattern.matcher(password);

        if(!idmatcher.matches()){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("아이디는 영문자(대소문자)로 시작하고 길이는 4~12자여야 합니다."))
                    .build();
        }

        if(!passwordmatcher.matches()){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("비밀번호는 6~20자이어야 합니다"))
                    .build();
        }

        if(email.length() > 100){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("이메일이 100자 이상입니다."))
                    .build();
        }

        if(username.length() > 100){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("이름이 100자 이상입니다."))
                    .build();
        }

        if(userDAO.getUser(id) !=null){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("아이디가 중복되었습니다"))
                    .build();
        }
        userDAO.insertUser(id, username, email, password);

        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/")
                .addheader("Content-Type", "text/html; charset=utf-8")
                .build();
    }
}

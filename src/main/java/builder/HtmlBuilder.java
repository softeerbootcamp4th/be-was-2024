package builder;

import handler.ResourceHandler;

import java.io.IOException;

public class HtmlBuilder {
    public String buildLoginSuccessHtml(String path) throws IOException {
        ResourceHandler resourceHandler = new ResourceHandler();
        String template = new String(resourceHandler.getByteArray(path));
        return template;
    }

    public String generateHtml(boolean isLoggedIn, String username) throws IOException {
        String templateFilePath = "/index.html"; // 템플릿 파일 경로
        ResourceHandler resourceHandler = new ResourceHandler();
        String template = new String(resourceHandler.getByteArray(templateFilePath));

        String loginButtonText, loginButtonHref, registrationButtonText, registrationButtonHref, userName;

        if (isLoggedIn) {
            loginButtonText = "글쓰기";
            loginButtonHref = "/article";
            registrationButtonText = "로그아웃";
            registrationButtonHref = "/logout";
            userName = username;
        } else {
            loginButtonText = "로그인";
            loginButtonHref = "/login";
            registrationButtonText = "회원가입";
            registrationButtonHref = "/registration";
            userName = "";
        }

        template = template.replace("login_button_text", loginButtonText)
                .replace("login_button_href", loginButtonHref)
                .replace("registration_button_text", registrationButtonText)
                .replace("registration_button_href", registrationButtonHref)
                .replace("username", userName);

        return template;
    }
}

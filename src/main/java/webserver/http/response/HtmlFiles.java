package webserver.http.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum HtmlFiles {
    //html하고 앞쪽 root부분 바꾸기
    LOGIN_FAILED("/login/login_failed"),
    LOGIN_SUCCESS("/main/index"),
    MAIN_PAGE("/index"),
    LOGIN("/login/index"),
    REGISTER("/registration/index"),
    REGISTER_FAILED("/registration/registration_failed"),
    USER_LIST("/user/userlist"),
    WRITE("/article/index");

    private static final String root= "./src/main/resources/static";
    private static final String html = ".html";

    private final String HtmlPath;

    HtmlFiles(String HtmlPath) {
        this.HtmlPath = HtmlPath;
    }

    private String getHtmlPath(){
        return this.HtmlPath;
    }

    public static String readHtmlString(HtmlFiles HtmlPath) throws IOException {
        return Files.readString(new File(root +  HtmlPath.getHtmlPath() + html).toPath());
    }

    public static byte[] readHtmlByte(HtmlFiles HtmlPath) throws IOException {
        return Files.readAllBytes(new File(root +  HtmlPath.getHtmlPath() + html).toPath());
    }

}

package webserver;

import db.BoardDatabase;
import db.Database;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enumPackage.ContentType;
import webserver.enumPackage.HtmlTemplate;
import webserver.enumPackage.HttpStatus;
import webserver.enumPackage.HttpVersion;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Http의 응답을 담당하는 클래스
 * 해당되는 응답 형식에 맞는 데이터 반환
 */
public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private HttpRequest httpRequest;
    private DataOutputStream dos;

    /**
     * 객체 생성 메서드
     * @param httpRequest Http 요청에 대한 정보
     * @param dos DataOutputStream에 대한 지정
     */
    public HttpResponse(HttpRequest httpRequest, DataOutputStream dos) {
        this.httpRequest = httpRequest;
        this.dos = dos;
    }

    /**
     * 정해진 경로를 받아 리다이렉션 해주는 메서드
     * @param redirectPath 리다이렉트 해줘야 되는 경로 정보
     * @throws IOException
     */
    public void redirectPath(String redirectPath) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(), "Location: " + redirectPath + "\r\n", null);
    }

    /**
     * 리다이렉트와 세션 설정을 해주는 메서드
     * @param sessionId 난수로 설정된 세션 id 설정
     * @param redirectPath 리다이렉트 해줘야 되는 경로 정보
     * @throws IOException
     */
    public void setCookieAndRedirectPath(String sessionId, String redirectPath) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(),
                "Location: " + redirectPath + "\r\nSet-Cookie: sid=" + sessionId + "; Path=/\r\n", null);
    }

    /**
     * 리다이렉트와 세션 초기화 설정을 해주는 메서드
     * @param redirectPath 리다이렉트 해줘야 되는 경로 정보
     * @throws IOException
     */
    public void resetCookieAndRedirectPath(String redirectPath) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(),
                "Location: " + redirectPath + "\r\nSet-Cookie: sid=; Path=/; Max-Age=0\r\n", null);
    }

    /**
     * 지정된 경로를 열어주는 메서드
     * @param url 오픈해줘야 하는 경로 정보
     * @throws IOException
     */
    public void openPath(String url) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String contentType = ContentType.fromExtension(getFileExtension(file));

            response200Header(fileBody.length, contentType);
            responseBody(fileBody);
        }
    }

    /**
     * 지정된 경로를 유저 리스트와 같이 열어주는 메서드
     * @param url 오픈해줘야 하는 경로 정보
     * @param username 사용자 정보 검사를 위한 매개변수
     * @throws IOException
     */
    public void openPathWithUsername(String url, String username) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String content = new String(fileBody);
            content = content.replace("{{username}}", username);
            byte[] modifiedFileBody = content.getBytes();

            String contentType = ContentType.fromExtension(getFileExtension(file));

            response200Header(modifiedFileBody.length, contentType);
            responseBody(modifiedFileBody);
        }
    }

    /**
     * 유저 리스트를 보여주는 메서드
     * @throws IOException
     */
    public void openUserList() throws IOException {
        Map<String, User> users = Database.findAll();

        StringBuilder userListHtml = new StringBuilder();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userListHtml.append("<li> ID: ").append(entry.getKey())
                    .append(" & Name: ").append(entry.getValue().getName())
                    .append("</li>");
        }
        String userListContent = HtmlTemplate.USER_LIST.getTemplate().replace("{{userList}}", userListHtml.toString());
        byte[] fileBody = userListContent.getBytes("UTF-8");

        response200Header(fileBody.length, ContentType.HTML.getMimeType());
        responseBody(fileBody);
    }

    /**
     * 404 에러 페이지를 띄워즈는 메서드
     * @throws IOException
     */
    public void response404Header() throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.NOT_FOUND, ContentType.HTML.getMimeType(), null, "<h1>404 Not Found</h1>".getBytes("UTF-8"));
    }

    /**
     * 에러 페이지를 열어주는 메서드
     * @param html 에러 페이지에 대한 정보가 포함된 html
     * @throws IOException
     */
    public void responseErrorPage(String html) throws IOException {
        byte[] responseBytes = html.getBytes("UTF-8");
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.OK, ContentType.HTML.getMimeType(), "Content-Length: " + responseBytes.length + "\r\n", responseBytes);
    }

    /**
     * 게시판 정보가 포함된 페이지를 열어주는 메서드(로그인 상태 구분 포함)
     * @param user 로그인 상태 정보 파악을 위한 유저 객체
     * @throws IOException
     */
    public void showBoard(User user) throws IOException {
        List<Board> boards = BoardDatabase.findAllBoards();

        StringBuilder allBoardsHtml = new StringBuilder();
        if(user == null){
            allBoardsHtml.append(HtmlTemplate.BOARD_NOT_LOGIN_HEADER.getTemplate());
        }else{
            allBoardsHtml.append(HtmlTemplate.BOARD_LOGIN_HEADER.getTemplate().replace("{{username}}", user.getName()));
        }


        for (Board board : boards) {
            String postHtml = HtmlTemplate.BOARD_BODY.getTemplate()
                    .replace("{{userId}}", board.getUserId())
                    .replace("{{content}}", board.getContent())
                    .replace("{{imagePath}}", "/" + board.getFilePath());

            allBoardsHtml.append(postHtml);
        }

        allBoardsHtml.append(HtmlTemplate.BOARD_FINAL);

        byte[] fileBody = allBoardsHtml.toString().getBytes("UTF-8");
        response200Header(fileBody.length, ContentType.HTML.getMimeType());
        responseBody(fileBody);
    }

    /**
     * 잘못된 요청이 들어왔을 때 에러메세지 경고창과 함께 리다이렉트 해주는 메서드
     * @param errorMessage 클라이언트에게 표시해주고자 하는 에러 메세지
     * @param redirectUrl 리다이렉트 해줘야 되는 경로 정보
     * @throws IOException
     */
    public void sendErrorPage(String errorMessage, String redirectUrl) throws IOException {
        String errorPage = HtmlTemplate.ERROR_PAGE.getTemplate()
                .replace("{{errorMessage}}", errorMessage)
                .replace("{{redirectUrl}}", redirectUrl);
        responseErrorPage(errorPage);
    }

    private void responseBody(byte[] body) throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.OK, contentType, "Content-Length: " + lengthOfBodyContent + "\r\n", null);
    }

    private void sendResponse(HttpVersion version, HttpStatus status, String contentType, String headers, byte[] body) throws IOException {
        dos.writeBytes(version.getVersion() + " " + status.toString() + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        if (headers != null) {
            dos.writeBytes(headers);
        }
        dos.writeBytes("\r\n");
        if (body != null) {
            dos.write(body);
        }
        dos.flush();
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return lastIndex == -1 ? "" : name.substring(lastIndex + 1);
    }


}

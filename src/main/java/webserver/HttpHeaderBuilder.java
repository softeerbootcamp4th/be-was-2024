package webserver;

public class HttpHeaderBuilder {
    public static String response302header(String redirectpath){ //redirection
        return  "HTTP/1.1 302 \r\n" +
                "Location: " + redirectpath+ "\r\n"  +
                "Content-Type: text/html;charset=utf-8\r\n"+
                "\r\n";
    }

    public static String response200Header(int lengthOfBodyContent, String ContentType) {
        return  "HTTP/1.1 200 OK \r\n" +
                "Content-Type: " + ContentType + "\r\n"  +
                "Content-Length: " + lengthOfBodyContent + "\r\n" +
                "\r\n";
    }

    public static String response404Header() {
        return "HTTP/1.1 404 Not Found\r\n";
    }


    public static String response405Header() {
        return "HTTP/1.1 405 Unsupported content type\r\n";
    }

}

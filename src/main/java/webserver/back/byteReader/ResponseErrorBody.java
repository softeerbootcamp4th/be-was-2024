package webserver.back.byteReader;

public class ResponseErrorBody implements Body{
    private String contentType = "text/plain";
    private String errorCause;

    public ResponseErrorBody(String errorCause) {
        this.errorCause = errorCause;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }
    @Override
    public byte[] makeBytes() {
        return errorCause.getBytes();
    }
}

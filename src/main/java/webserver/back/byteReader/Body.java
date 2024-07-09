package webserver.back.byteReader;

public interface Body {
    public String getContentType();
    public byte[] makeBytes();
}

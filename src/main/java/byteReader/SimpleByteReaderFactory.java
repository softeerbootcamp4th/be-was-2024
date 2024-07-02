package byteReader;

public class SimpleByteReaderFactory {
    public ByteReader returnByteReader(String contentType){
        ByteReader byteReader = null;
        if(contentType.startsWith("text")) {
            contentType = contentType.substring("text".length()+1);
            byteReader = new StaticFileByteReader();
        }

        return byteReader;
    }
}

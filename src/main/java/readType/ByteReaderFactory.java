package readType;

public class ByteReaderFactory {
    public ByteReader returnByteReader(String mimeType){
        System.out.println(mimeType+":mimeType");
        ByteReader byteReader = null;
        if(mimeType.startsWith("text")) {
            mimeType =mimeType.substring("text".length()+1);
            if(mimeType.startsWith("html")){
                byteReader = new StaticHtmlByteReader();
            }
        }
        return byteReader;
    }
}

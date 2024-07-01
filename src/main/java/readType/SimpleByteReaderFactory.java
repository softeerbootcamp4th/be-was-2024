package readType;

import webserver.HttpRequest;

public class SimpleByteReaderFactory {
    public ByteReader returnByteReader(HttpRequest httpRequest){
        String mimeType = httpRequest.getMimeTypeForClient();
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

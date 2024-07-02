package returnType;

import webserver.HttpRequest;

import java.util.logging.Logger;

public class ContentTypeFactory {
    public ContentTypeFactory getContentTypeFactory(HttpRequest httpRequest){
        String url = httpRequest.getUrl();
        if(url.endsWith("html")){
            return new HTMLFactory();
        }
        else if(url.endsWith("css")){
            return new CSSFactory();
        }
        else if (url.startsWith("/img")){
            IMGFactory imgFactory = new IMGFactory();
            return imgFactory.getContentTypeFactory(httpRequest);
        }
        return new UNKNOWNContentFactory();
    }

    public String getContentType() {
        throw new IllegalArgumentException("contentType 정해지지 않음");
    }
}

class HTMLFactory extends ContentTypeFactory {
    @Override
    public String getContentType() {
        return "text/html";
    }
}
class CSSFactory extends ContentTypeFactory {
    @Override
    public String getContentType() {
        return "text/css";
    }
}
class UNKNOWNContentFactory extends ContentTypeFactory{
    @Override
    public String getContentType() {
        return"unknown content type";
    }
}

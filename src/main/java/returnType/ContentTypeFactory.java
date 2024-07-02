package returnType;

import webserver.HttpRequest;

public class ContentTypeFactory {
    public ContentTypeFactory getContentTypeFactory(HttpRequest httpRequest){
        String url = httpRequest.getUrl();
        if(url.endsWith("html")){
            return new HTMLFactory();
        }
        else if(url.endsWith("css")){
            return new CSSFactory();
        }
        return new UNKNOWNContentFactory();
    }

    public String getContentType() {
        return null;
    }
}

class UNKNOWNContentFactory extends ContentTypeFactory{
    @Override
    public String getContentType() {
        return"unknown content type";
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
class IMGFactory extends ContentTypeFactory {
    @Override
    public String getContentType() {
        return "text/html";
    }
}

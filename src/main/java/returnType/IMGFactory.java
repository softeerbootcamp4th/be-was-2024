package returnType;

import webserver.HttpRequest;

public class IMGFactory extends ContentTypeFactory{

    protected final String imageBaseUrl = "image/";
    @Override
    public ContentTypeFactory getContentTypeFactory(HttpRequest httpRequest) {
       String url = httpRequest.getUrl();
       if(url.endsWith("png")) return new PNGFactory();
       else if(url.endsWith("jpg")) return new JPGFactory();
       else if (url.endsWith("ico")) return new ICOFactory();
       else if(url.endsWith("svg")) return new SVGFactory();
       return null;
    }
}
class SVGFactory extends IMGFactory{
    @Override
    public String getContentType() {
        return imageBaseUrl+"svg+xml";
    }
}
class PNGFactory extends IMGFactory{
    @Override
    public String getContentType() {
        return imageBaseUrl + "png";
    }
}
class JPGFactory extends IMGFactory{
    @Override
    public String getContentType() {
        return imageBaseUrl +"jpeg";
    }
}
class ICOFactory extends IMGFactory{
    @Override
    public String getContentType() {
        return imageBaseUrl +"x-icon";
    }
}

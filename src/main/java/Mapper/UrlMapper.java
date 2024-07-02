package Mapper;

public class UrlMapper {
    public String getMappedUrl(String originalUrl){
        if(originalUrl.equals("/registration")) return "/registration/index.html";
        return originalUrl;
    }
}

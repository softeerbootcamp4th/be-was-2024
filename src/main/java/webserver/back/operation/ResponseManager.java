package webserver.back.operation;

import webserver.back.data.RequestInformation;
import webserver.back.data.SignInForm;
import webserver.back.fileFounder.StaticFileFounder;
import webserver.back.byteReader.ByteReader;
import webserver.front.data.HttpResponse;

import java.io.FileNotFoundException;

public class ResponseManager {
    private final UserMapper userMapper;
    private final URIParser uriParser;
    public ResponseManager(UserMapper userMapper){
        this.userMapper = userMapper;
        uriParser = new URIParser();
    }
    public HttpResponse getResponse(String originalUrl)  {
        ResponseDataMaker responseDataMaker = new ResponseDataMaker();
        String message = "";
        ByteReader byteReader = null;
        try{
            String changedPath;
            RequestInformation requestInformation = uriParser.getParsedUrl(originalUrl);
            String path = requestInformation.getPath()[1];
            if (path.equals("registration")) {
                changedPath = "registration/index.html";
                byteReader = new StaticFileFounder().findFile(changedPath);
                message ="OK";
                return responseDataMaker.makeHttpResponse(byteReader,message);
            }
            if (path.equals("create")) {
                SignInForm signInForm = new SignInForm(requestInformation.getInformation());
                byteReader= userMapper.addUser(signInForm);
                message ="FOUND";
                String location ="/index.html";
                return responseDataMaker.makeHttpResponse(byteReader,message,location);
            }
            if(path.equals("index.html")){
                byteReader = new StaticFileFounder().findFile(path);
                message ="OK";
                return responseDataMaker.makeHttpResponse(byteReader,message);
            }
            message ="NOT_FOUND";
            return responseDataMaker.makeHttpResponse(byteReader,message);
        }
        catch (Exception e){
            message = "ERROR";
            return responseDataMaker.makeHttpResponse(byteReader,message);
        }
    }
}


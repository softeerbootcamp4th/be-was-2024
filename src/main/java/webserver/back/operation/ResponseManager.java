package webserver.back.operation;

import webserver.back.data.RequestInformation;
import webserver.back.data.SignInForm;
import webserver.back.data.StatusCode;
import webserver.back.fileFounder.StaticFileFounder;
import webserver.back.byteReader.ByteReader;
import webserver.front.data.HttpResponse;

import java.io.FileNotFoundException;

public class ResponseManager {
    private final UserMapper userMapper;
    public ResponseManager(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    public HttpResponse getResponse(String originalUrl)  {
        ResponseDataMaker responseDataMaker = new ResponseDataMaker();
        String message = "";
        ByteReader byteReader;
        try{
            String changedPath;
            RequestInformation requestInformation = URIParser.getParsedUrl(originalUrl);
            String path = requestInformation.getPath()[1];
            if (path.equals("registration")) {
                changedPath = "/registration/index.html";
                byteReader = new StaticFileFounder().findFile(changedPath);
                message = StatusCode.OK.getMessage();
                return responseDataMaker.makeHttpResponse(byteReader,message);
            }
            if (path.equals("create")) {
                SignInForm signInForm = new SignInForm(requestInformation.getInformation());
                byteReader= userMapper.addUser(signInForm);
                message =StatusCode.FOUND.getMessage();
                String location ="/index.html";
                return responseDataMaker.makeHttpResponse(byteReader,message,location);
            }
            else {
                byteReader = new StaticFileFounder().findFile(originalUrl);
                message =StatusCode.OK.getMessage();
                return responseDataMaker.makeHttpResponse(byteReader,message);
            }
        }
        catch (FileNotFoundException e){ //404 Not Found를 위한 곳
            message =StatusCode.NOT_FOUND.getMessage();
            byteReader = null;
            return responseDataMaker.makeHttpResponse(byteReader,message);
        }
        catch (Exception e){
            message = StatusCode.ERROR.getMessage();
            byteReader =null;
            return responseDataMaker.makeHttpResponse(byteReader,message);
        }
    }
}


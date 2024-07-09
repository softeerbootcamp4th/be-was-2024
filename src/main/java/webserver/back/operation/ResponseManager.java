package webserver.back.operation;

import webserver.back.Error.WrongDataFormatException;
import webserver.back.data.PathInformation;
import webserver.back.data.SignInForm;
import webserver.back.data.StatusCode;
import webserver.back.fileFounder.StaticFileFounder;
import webserver.back.byteReader.Body;
import webserver.front.data.HttpResponse;

import java.io.FileNotFoundException;

public class ResponseManager {
    private final UserMapper userMapper;
    public ResponseManager(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    public HttpResponse getResponse(String originalUrl)  {
        ResponseDataMaker responseDataMaker = new ResponseDataMaker();
        String message;
        Body body;
        try{
            String changedPath;
            PathInformation pathInformation = URIParser.getParsedUrl(originalUrl);
            String path = pathInformation.getPathArr();
            System.out.println(path);
            if (path.equals("/registration")) {
                changedPath = "/registration/index.html";
                body = new StaticFileFounder().findFile(changedPath);
                message = StatusCode.OK.getMessage();
                return responseDataMaker.makeHttpResponse(body,message);
            }
            if (path.equals("/create")) {
                SignInForm signInForm = new SignInForm(pathInformation.getInformation());
                body = userMapper.addUser(signInForm);
                message =StatusCode.FOUND.getMessage();
                String location ="/index.html";
                return responseDataMaker.makeHttpResponse(body,message,location);
            }
            body = new StaticFileFounder().findFile(originalUrl);
            message =StatusCode.OK.getMessage();
            return responseDataMaker.makeHttpResponse(body,message);
        }
        catch (FileNotFoundException e){ //404 Not Found를 위한 곳
            message =StatusCode.NOT_FOUND.getMessage();
            return responseDataMaker.makeHttpResponseError(message,e.getMessage());
        }
        catch (WrongDataFormatException e){
            message = StatusCode.BAD_REQUEST.getMessage();
            return responseDataMaker.makeHttpResponseError(message,e.getMessage());
        }
        catch (Exception e){
            message = StatusCode.ERROR.getMessage();
            return responseDataMaker.makeHttpResponseError(message,e.getMessage());
        }
    }
}


package webserver.back.operation;

import webserver.back.data.RequestInformation;
import webserver.back.data.SignInForm;
import webserver.back.fileFounder.FileFounder;
import webserver.back.fileFounder.StaticFileFounder;
import webserver.back.byteReader.ByteReader;
import webserver.back.returnType.ContentTypeMaker;
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
        String message = "";
        ByteReader byteReader = null;
        try{
            String changedUrl;
            RequestInformation requestInformation = uriParser.getParsedUrl(originalUrl);
            if (requestInformation.getPath()[1].equals("registration")) {
                changedUrl = "registration/index.html";
                byteReader = new StaticFileFounder().findFile(changedUrl);
                message ="OK";
            }
            else if (requestInformation.getPath()[1].equals("create")) {
                SignInForm signInForm = new SignInForm(requestInformation.getInformation());
                byteReader= userMapper.addUser(signInForm);
                message ="OK";
            }
            else{
                byteReader = new StaticFileFounder().findFile(originalUrl);
                message ="OK";
            }
        }
        catch (FileNotFoundException e){
            message = "NOT_FOUND";
        }
        catch (Exception e){
            message = "ERROR";
        }
        ResponseDataMaker responseDataMaker = new ResponseDataMaker(byteReader, message);
        return responseDataMaker.getHttpResponse();
    }
}


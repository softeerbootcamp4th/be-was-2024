package Mapper;

import byteReader.ByteReader;
import requestForm.SignInForm;
import returnType.ContentTypeMaker;
import webserver.HttpResponse;
import webserver.ResponseDataMaker;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class ResponseManager {
    private final UserMapper userMapper;
    private final URIParser uriParser;
    private ResponseDataMaker responseDataMaker;
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
            System.out.println(requestInformation.getPath()[1]);
            if (requestInformation.getPath()[1].equals("registration")) {
                changedUrl = "registration/index.html";
                byteReader= new StaticFileFounder().findFile(changedUrl, ContentTypeMaker.getContentType(changedUrl));
                message ="OK";
            }
            else if (requestInformation.getPath()[1].equals("create")) {
                SignInForm signInForm = new SignInForm(requestInformation.getInformation());
                byteReader= userMapper.addUser(signInForm);
                message ="OK";
            }
            else{
                byteReader = new StaticFileFounder().findFile(originalUrl, ContentTypeMaker.getContentType(originalUrl));
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
class URIParser{
    public RequestInformation getParsedUrl(String originalUrl){
        String[] uriSeperated = originalUrl.split("\\?");
        String pathUrl = uriSeperated[0];
        String[] pathSeperated = pathUrl.split("/");
        if(uriSeperated.length>1){
            HashMap<String,String> map = parseParam(uriSeperated);
            return new RequestInformation(pathSeperated,true,map);
        }
        return new RequestInformation(pathSeperated,false,null);
    }
    private static HashMap<String, String> parseParam(String[] uriSeperated) {
        String[] paramSeperated = uriSeperated[1].split("&");
        HashMap<String, String> map = new HashMap<>();
        for (String param : paramSeperated) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
class RequestInformation{
    private String[] path;
    private boolean ifAdditionalInformationExist;
    private HashMap<String,String> information;

    public String[] getPath() {
        return path;
    }

    public HashMap<String, String> getInformation() {
        return information;
    }

    public boolean IfAdditionalInformationExist() {
        return ifAdditionalInformationExist;
    }

    public RequestInformation(String[] path, boolean ifAdditionalInformationExist, HashMap<String,String> information) {
        this.path = path;
        this.ifAdditionalInformationExist = ifAdditionalInformationExist;
        this.information = information;
    }
}
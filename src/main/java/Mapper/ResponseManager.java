package Mapper;

import byteReader.ByteReader;
import byteReader.StaticFileReader;
import requestForm.SignInForm;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

public class ResponseManager {
    private final UserMapper userMapper;
    private final URIParser uriParser;
    public ResponseManager(UserMapper userMapper){
        this.userMapper = userMapper;
        uriParser = new URIParser();
    }
    public ByteReader getByte(String originalUrl) throws FileNotFoundException {
        try{
            RequestInformation requestInformation = uriParser.getParsedUrl(originalUrl);
            if (requestInformation.getPath()[1].equals("registration")) {
                return new StaticFileFounder().findFile("registration/index.html");
            }
            if (requestInformation.getPath()[1].equals("create")) {
                SignInForm signInForm = new SignInForm(requestInformation.getInformation());
                return userMapper.addUser(signInForm);
            }
            return new StaticFileFounder().findFile(originalUrl);
        }
        catch (Exception e){
            e.printStackTrace();
            return new StaticFileFounder().findFile("404Page.html");
        }
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
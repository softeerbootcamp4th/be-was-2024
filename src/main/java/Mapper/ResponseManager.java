package Mapper;

import db.Database;
import model.User;
import requestForm.SignInForm;

import java.util.HashMap;

public class ResponseManager {
    private final UserMapper userMapper;
    public ResponseManager(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public String getMappedUrl(String originalUrl) {
        String[] uriSeperated = originalUrl.split("\\?");
        String pathUrl = uriSeperated[0];
        String[] pathSeperated = pathUrl.split("/");

        if (pathSeperated[1].equals("registration")) return "registration/index.html";
        if (pathSeperated[1].equals("create")) {
            String[] paramSeperated = uriSeperated[1].split("&");
            HashMap<String, String> map = new HashMap<>();
            for (String param : paramSeperated) {
                String[] keyValue = param.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            SignInForm signInForm = new SignInForm(map);
            userMapper.saveUser(signInForm);
            for(User user : Database.findAll()){
                System.out.println(user.getUserId());
            }
            return originalUrl;
        }
        return pathUrl;
    }
}

package webserver.back.data;

import webserver.back.Error.WrongDataFormatException;

import java.util.HashMap;

public class SignInForm {

    private String name;
    private String password;
    private String userId;
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public SignInForm(HashMap<String,String> informations) throws WrongDataFormatException {
        this.name = informations.get("name");
        this.password = informations.get("password");
        this.userId = informations.get("userId");
        checkNotNull();
    }
    public void checkNotNull() throws WrongDataFormatException{
        if(name==null||password==null||userId==null) throw new WrongDataFormatException("data should not be null");
    }
}

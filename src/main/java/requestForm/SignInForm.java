package requestForm;

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

    public SignInForm(HashMap<String,String> informations) {
        this.name = informations.get("name");
        this.password = informations.get("password");
        this.userId = informations.get("userId");
    }
}

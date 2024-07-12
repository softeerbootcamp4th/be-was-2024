package data;

/**
 *회원가입 바디
 */
public class RegistrationRequest {
    private String userId;
    private String email;
    private String password;
    private String name;

    public RegistrationRequest(String userId, String email, String password, String name) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}

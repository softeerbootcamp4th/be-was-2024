package dto;

import model.User;

public class UserDTO {
    private boolean isValid;
    private String userId;
    private String name;
    private String email;

    public UserDTO(User user, boolean isValid){
        this.isValid = isValid;
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public boolean isValid() {
        return isValid;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
}

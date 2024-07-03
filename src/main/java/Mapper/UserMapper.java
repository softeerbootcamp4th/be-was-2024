package Mapper;

import db.Database;
import model.User;
import requestForm.SignInForm;

public class UserMapper {
    public UserMapper() {
    }

    public void saveUser(SignInForm signInForm) {
        Database.addUser(new User(signInForm.getUserId(), signInForm.getPassword(),signInForm.getName(),"email"));
    }

}

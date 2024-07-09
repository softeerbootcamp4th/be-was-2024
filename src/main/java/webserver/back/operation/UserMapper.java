package webserver.back.operation;

import webserver.back.data.SignInForm;
import webserver.back.byteReader.Body;
import webserver.back.byteReader.ResponseJsonBody;
import webserver.back.db.Database;
import webserver.back.model.User;

public class UserMapper {
    public UserMapper() {
    }

    public Body addUser(SignInForm signInForm) {
        User newUser = Database.addUser(new User(signInForm.getUserId(), signInForm.getPassword(),signInForm.getName(),"email"));
        return new ResponseJsonBody(newUser);
    }

}

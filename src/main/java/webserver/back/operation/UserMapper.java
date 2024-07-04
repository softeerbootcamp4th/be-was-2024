package webserver.back.operation;

import webserver.back.data.SignInForm;
import webserver.back.byteReader.ByteReader;
import webserver.back.byteReader.JsonReader;
import webserver.back.db.Database;
import webserver.back.model.User;

public class UserMapper {
    public UserMapper() {
    }

    public ByteReader addUser(SignInForm signInForm) {
        User newUser = Database.addUser(new User(signInForm.getUserId(), signInForm.getPassword(),signInForm.getName(),"email"));
        return new JsonReader(newUser);
    }

}

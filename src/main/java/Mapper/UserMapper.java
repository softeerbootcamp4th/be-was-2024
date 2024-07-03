package Mapper;

import byteReader.ByteReader;
import byteReader.JsonReader;
import db.Database;
import model.User;
import requestForm.SignInForm;

public class UserMapper {
    public UserMapper() {
    }

    public ByteReader addUser(SignInForm signInForm) {
        User newUser = Database.addUser(new User(signInForm.getUserId(), signInForm.getPassword(),signInForm.getName(),"email"));
        return new JsonReader(newUser);
    }

}

package request;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Properties;


public class GetRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRequestHandler.class);
    static Properties properties = new Properties();
    private static String staticPath;

    private static void createUser(String param, OutputStream out) {
        String[] params = param.split("&");

        String userId = params[0].split("=")[1];
        String password = params[1].split("=")[1];
        String name = params[2].split("=")[1];
        String email = params[3].split("=")[1];

        Database.addUser(new User(userId, password, name, email));

        DataOutputStream dos = new DataOutputStream(out);
//        ResponseFactory.response302Header(dos, "/");
    }

}

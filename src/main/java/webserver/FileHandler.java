package webserver;

import model.HttpStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    static class ResponseWithStatus {
        HttpStatus status;
        byte[] body;
        ResponseWithStatus(HttpStatus status, byte[] body){
            this.status = status;
            this.body = body;
        }
    }
    static ResponseWithStatus getFileContent(String path) throws IOException {
        try{

            StringBuilder file = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String fileLine = br.readLine();

            while (fileLine != null) {
                file.append(fileLine);
                fileLine = br.readLine();
            }
            return new ResponseWithStatus(HttpStatus.OK, file.toString().getBytes());

        }catch (FileNotFoundException e){

            String notFound = "<h1>Page Not Found</h1>";
            return new ResponseWithStatus(HttpStatus.NOT_FOUND, notFound.getBytes());

        }
    }
}

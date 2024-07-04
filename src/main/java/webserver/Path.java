package webserver;

import exception.NotExistException;

public class Path {

    private String path;

    public Path(String path){
        this.path = path;
    }

    public String getExtension(){
        String extension = "";
        try {
            extension = path.split("\\.")[1];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new NotExistException();
        }
        return extension;
    }

    public boolean isStatic(){

        try {
            getExtension();
        }catch (NotExistException e){
            return false;
        }

        return true;
    }

}

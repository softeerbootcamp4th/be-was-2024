package webserver.request;

import exception.NotExistException;

import java.util.Optional;

public class Path {

    private final String path;
    private final Optional<Parameter> parameter;

    public Path(String path){
        this.path = path;
        String[] splitPath = path.split("\\?");
        String parameters;
        try{
            parameters = splitPath[1];
        }catch (ArrayIndexOutOfBoundsException e){
            this.parameter = Optional.empty();
            return;
        }
        this.parameter = Optional.of(new Parameter(parameters));
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

    public Optional<Parameter> getParameter(){
        return this.parameter;
    }

}

package webserver;

public class Path {

    private String path;

    public Path(String path){
        this.path = path;
    }

    public String getExtension(){
        return path.split("\\.")[1];
    }

}

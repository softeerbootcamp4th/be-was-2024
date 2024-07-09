package webserver.back.Error;

public class WrongDataFormatException extends Exception{
    public WrongDataFormatException(){
        super("wrong data format");
    }
    public WrongDataFormatException(String message){
        super(message);
    }
}

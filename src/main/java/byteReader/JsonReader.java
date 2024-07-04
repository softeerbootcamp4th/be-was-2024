package byteReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class JsonReader implements ByteReader{
    private Object object;
    private String contentType = "application/json";
    public JsonReader(Object object){
        this.object = object;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] readBytes()  {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e){
            return new byte[0];
        }

    }
}

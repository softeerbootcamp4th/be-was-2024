package byteReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class JsonReader implements ByteReader{
    private Object object;
    public JsonReader(Object object){
        this.object = object;
    }
    @Override
    public byte[] readBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }
}

package Mapper;

import byteReader.ByteReader;
import byteReader.StaticFileReader;

import java.io.FileNotFoundException;

public class StaticFileFounder implements FileFounder{
    private String FILE_BASE_URL ="./src/main/resources/static/";

    @Override
    public ByteReader findFile(String fileUrl) throws FileNotFoundException {
        return new StaticFileReader(FILE_BASE_URL + fileUrl);
    }
}

package Mapper;

import byteReader.ByteReader;

import java.io.FileNotFoundException;

public interface FileFounder {

    ByteReader findFile(String fileUrl,String contentType) throws FileNotFoundException;
}

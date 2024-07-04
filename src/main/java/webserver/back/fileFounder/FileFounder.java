package webserver.back.fileFounder;

import webserver.back.byteReader.ByteReader;

import java.io.FileNotFoundException;

public interface FileFounder {

    ByteReader findFile(String fileUrl) throws FileNotFoundException;
}

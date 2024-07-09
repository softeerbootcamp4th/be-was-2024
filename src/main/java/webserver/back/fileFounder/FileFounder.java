package webserver.back.fileFounder;

import webserver.back.byteReader.Body;

import java.io.FileNotFoundException;

public interface FileFounder {

    Body findFile(String fileUrl) throws FileNotFoundException;
}

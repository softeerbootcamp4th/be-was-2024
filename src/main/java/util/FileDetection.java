package util;

import java.io.File;

public class FileDetection
{

    public static String fixedPath = "src/main/resources/static";


    public static String getPath(String path)
    {
        File fi = new File(path);
        return fi.isDirectory()? path+"/index.html" : path;
    }
}

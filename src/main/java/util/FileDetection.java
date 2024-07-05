package util;

import java.io.File;

public class FileDetection
{

    public static String fixedPath = "src/main/resources/static";


    public static String getPath(String path)
    {

        File file = new File(path);
        return file.isDirectory()? path+"/index.html" : path;
    }
}

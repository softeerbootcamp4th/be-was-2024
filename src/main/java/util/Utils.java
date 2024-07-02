package util;

import java.io.*;

public class Utils {

    public static byte[] getFile(String url) throws IOException {

        File file = new File("src/main/resources/static/" + url);

        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int len;
        byte[] buf = new byte[1024];
        while((len = inputStream.read(buf))!=-1){
            byteArrayOutputStream.write(buf, 0, len);
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static String getAllStrings(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        while(true){
            String inputLine = br.readLine();
            if(inputLine!=null) {
                if(inputLine.isEmpty()) break;
            }else break;
            sb.append(inputLine)
                    .append("\n");
        }

        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public static String getUrl(String request) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(request));
        String url = br.readLine();
        return url.split(" ")[1].substring(1);
    }

}

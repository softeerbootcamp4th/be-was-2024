package webserver.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BodyContentDecomposision {
    private static final byte[] NEWLINE = {'\r','\n'};

    public static Map<String, FileData> decompose(byte[] body, String boundary) {
        Map<String, FileData> map = new HashMap<>();
        String boundaryString = "--" + boundary;
        byte[] boundaryBytes = boundaryString.getBytes();
        byte[] doubleCRLF = "\r\n\r\n".getBytes();

        int startIndex = 0;
        int endIndex;

        while ((endIndex = indexOf(body, boundaryBytes, startIndex)) != -1) {
            int partLength = endIndex - startIndex;
            if (partLength <= 0) {
                startIndex = endIndex + boundaryBytes.length;
                continue;
            }

            byte[] partData = Arrays.copyOfRange(body, startIndex, endIndex);
            int headerEndIndex = indexOf(partData, doubleCRLF, 0) + doubleCRLF.length;

            if (headerEndIndex == -1) {
                startIndex = endIndex + boundaryBytes.length;
                continue;
            }

            String partHeader = new String(partData, 0, headerEndIndex);
            String partName = null;
            String fileName = null;
            String[] headers = partHeader.split("\r\n");
            for (String header : headers) {
                if (header.startsWith("Content-Disposition:")) {
                    int nameIndex = header.indexOf("name=\"") + 6;
                    int nameEndIndex = header.indexOf("\"", nameIndex);
                    partName = header.substring(nameIndex, nameEndIndex);

                    int fileNameIndex = header.indexOf("filename=\"") + 10;
                    if (fileNameIndex != 9) {  // if "filename=\"" is found
                        int fileNameEndIndex = header.indexOf("\"", fileNameIndex);
                        fileName = header.substring(fileNameIndex, fileNameEndIndex);
                    }
                }
            }

            if (partName != null) {
                byte[] fileData = Arrays.copyOfRange(partData, headerEndIndex, partLength - 2);
                map.put(partName, new FileData(fileName, fileData));
            }

            startIndex = endIndex + boundaryBytes.length;
        }
        return map;
    }
    private static int indexOf(byte[] data, byte[] pattern, int start) {
        for (int i = start; i < data.length - pattern.length + 1; i++) {
            boolean found = true;
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }
}

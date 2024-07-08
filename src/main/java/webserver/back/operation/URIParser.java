package webserver.back.operation;

import webserver.back.data.RequestInformation;

import java.util.HashMap;

public class URIParser {
    public RequestInformation getParsedUrl(String originalUrl) {
        String[] uriSeperated = originalUrl.split("\\?");
        String pathUrl = uriSeperated[0];
        String[] pathSeperated = pathUrl.split("/");
        if (uriSeperated.length > 1) {
            HashMap<String, String> map = parseParam(uriSeperated);
            return new RequestInformation(pathSeperated, true, map);
        }
        return new RequestInformation(pathSeperated, false, null);
    }

    private static HashMap<String, String> parseParam(String[] uriSeperated) {
        String[] paramSeperated = uriSeperated[1].split("&");
        HashMap<String, String> map = new HashMap<>();
        for (String param : paramSeperated) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
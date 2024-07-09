package webserver.back.data;

import java.util.Arrays;
import java.util.HashMap;

public class PathInformation {
    private String[] pathArr;
    private boolean ifAdditionalInformationExist;
    private HashMap<String, String> information;

    public String getPathArr() {
        StringBuilder path = new StringBuilder();
        for(String str : pathArr){
            path.insert(0, "/");
            path.append(str);
        }
        return path.toString();
    }

    public HashMap<String, String> getInformation() {
        return information;
    }

    public boolean IfAdditionalInformationExist() {
        return ifAdditionalInformationExist;
    }

    public PathInformation(String[] pathArr, boolean ifAdditionalInformationExist, HashMap<String, String> information) {
        this.pathArr = Arrays.copyOfRange(pathArr,1,pathArr.length);
        this.ifAdditionalInformationExist = ifAdditionalInformationExist;
        this.information = information;
    }
}

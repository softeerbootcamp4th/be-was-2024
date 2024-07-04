package webserver.back.data;

import java.util.HashMap;

public class RequestInformation {
    private String[] path;
    private boolean ifAdditionalInformationExist;
    private HashMap<String, String> information;

    public String[] getPath() {
        return path;
    }

    public HashMap<String, String> getInformation() {
        return information;
    }

    public boolean IfAdditionalInformationExist() {
        return ifAdditionalInformationExist;
    }

    public RequestInformation(String[] path, boolean ifAdditionalInformationExist, HashMap<String, String> information) {
        this.path = path;
        this.ifAdditionalInformationExist = ifAdditionalInformationExist;
        this.information = information;
    }
}

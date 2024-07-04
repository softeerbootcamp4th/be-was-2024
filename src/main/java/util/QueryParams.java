package util;

import java.util.HashMap;
import java.util.Map;

public class QueryParams {


    private final String path;

    public QueryParams(String path)
    {
        this.path = path;
    }
    public Map<String, String> getQueryParams()
    {
        Map<String, String> queryParams = new HashMap<>();
        if (path == null || path.isEmpty()) {
            return queryParams;
        }
        int idx = path.indexOf('?');
        if (idx < 0) {
            return queryParams;
        }
        String queryString = path.substring(idx + 1);
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }
}

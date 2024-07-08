package util;

public class UrlMapper {

    /**
     * Maps the given URL to a new URL.
     *
     * @param url the URL to map
     * @return the new URL
     */
    public String mapUrl(String url) {
        return UrlMapping.map(url);
    }
}

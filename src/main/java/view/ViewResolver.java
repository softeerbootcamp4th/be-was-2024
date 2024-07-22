package view;

import java.io.IOException;

public class ViewResolver {
    private final String prefix;
    private final String suffix;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public View resolve(String viewName) throws IOException {
        String path = prefix + viewName + suffix;
        return new View(path);
    }
}

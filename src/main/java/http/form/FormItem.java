package http.form;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record FormItem(String filename, byte[] data) {
    public String dataAsText(Charset charset) {
        return new String(data, charset);
    }

    public String dataAsText() {
        return dataAsText(StandardCharsets.UTF_8);
    }
}

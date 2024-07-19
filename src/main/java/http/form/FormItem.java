package http.form;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * form에서 가져온 데이터를 표현하는 클래스
 * @param filename
 * @param data
 */
public record FormItem(String filename, byte[] data) {

    /**
     * 데이터를 text로 변경해주는 유틸 기능
     * @return
     */
    public String dataAsText(Charset charset) {
        return new String(data, charset);
    }

    public String dataAsText() {
        return dataAsText(StandardCharsets.UTF_8);
    }
}

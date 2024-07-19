package chain;

import chain.core.MiddlewareChain;
import http.form.FormItem;
import http.HeaderConst;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.MIMEType;
import http.form.FormDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * content-type = multipart/form-data 인 요청이 들어왔을 때, 요청 데이터를 파싱하는 체인
 */
public class MultipartHandleChain extends MiddlewareChain {
    private static final Logger logger = LoggerFactory.getLogger(MultipartHandleChain.class);
    public static final String boundaryRegex = "multipart/form-data\\s*;\\s*boundary\\s*=\\s*(.*)";
    public static final String itemNameRegex = "Content-Disposition:\\s*form-data;\\s*name\\s*=\\s*\"(.*?)\"";
    public static final String fileNameRegex = "filename\\s*=\\s*\"(.*?)\"";
    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        String contentTypeValue = req.getHeaders().getHeader(HeaderConst.ContentType);

        // multipart/form-data 타입이 아님
        if(contentTypeValue == null || !contentTypeValue.startsWith(MIMEType.multipart_form_data.getMimeType())) {
            next(req, res);
            return;
        }

        // multipart 타입인 경우
        Pattern boundaryPattern = Pattern.compile(boundaryRegex);
        Matcher boundaryMatcher = boundaryPattern.matcher(contentTypeValue);

        Pattern itemNamePattern = Pattern.compile(itemNameRegex);
        Pattern fileNamePattern = Pattern.compile(fileNameRegex);

        if(!boundaryMatcher.find()) {
            next(req, res);
            return;
        }
        String boundary = boundaryMatcher.group(1);

        if(boundary == null) throw new RuntimeException("boundary 속성을 찾을 수 없음");
        boundary = "--" + boundary; // 각 바운더리 라인은 "--"으로 시작한다.
        // 라인을 읽었을 때 boundary가 등장할 때까지 읽어 넣기.
        String bodyStr = new String(req.getBody(), StandardCharsets.ISO_8859_1);
        String[] eachPart = bodyStr.split(boundary);

        try {
            // 마지막 값은 항상 -- ( 구분자 ) 이므로 무시
            // 첫 라인은 항상 공백 문자열이므로 무시
            for (int i = 1; i < eachPart.length - 1; i++) {
                BufferedReader br = new BufferedReader(new StringReader(eachPart[i].stripLeading()));
                String line = null;
                List<String> headers = new ArrayList<>();
                while (!(line = br.readLine()).isEmpty()) {
                    headers.add(line);
                }
                // 이름 파싱
                if(headers.isEmpty()) throw new RuntimeException("필수 헤더 없음");

                Matcher itemNameMatcher = itemNamePattern.matcher(headers.get(0));
                if(!itemNameMatcher.find()) throw new RuntimeException("필수 헤더 없음");
                String partName = itemNameMatcher.group(1);


                String filename = null;
                Matcher fileNameMatcher = fileNamePattern.matcher(headers.get(0));
                if(fileNameMatcher.find()) {
                    filename = fileNameMatcher.group(1);
                }

                // 바디 파싱
                StringBuilder partBodyStr = new StringBuilder();
                char[] bodyBuffer = new char[1024];
                int read;
                while((read = br.read(bodyBuffer)) != -1) {
                    partBodyStr.append(bodyBuffer, 0, read);
                }

                byte[] partBody = partBodyStr.toString().getBytes(StandardCharsets.ISO_8859_1);
                FormDataUtil.addFormData(req, partName, new FormItem(filename, partBody));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        next(req, res);
    }
}

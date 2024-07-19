package util;

import data.MultipartFile;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 멀티파트 파일을 다루는 유틸 클래스
 */
public class MultiPartUtils {
    private static final Logger log = LoggerFactory.getLogger(MultiPartUtils.class);

    /**
     * 멀티파트 요청에서 boundary정보를 가져온다.
     * @param headers
     * @return boundary byte 배열
     */
    public static byte[] getBoundary(Map<String,String> headers){
        String contentType = headers.get("Content-Type");
        String[] contentTypeSplit = contentType.split(";");
        for (String entry : contentTypeSplit) {
            String[] keyValue = entry.split("=");
            if (keyValue[0].strip().equals("boundary")) {
                return keyValue[1].strip().getBytes();
            }
        }
        return null;
    }

    /**
     * 멀티파트 파트들을 가져와서 Post객체에 넣는다.
     * 즉, 데이터베이스에 넣을 Post객체를 완성한다.
     * @param userId
     * @param userName
     * @param files
     * @return 멀티파트 정보가 입력된 Post객체
     */
    public static Post processMultiPart(Long userId, String userName, List<MultipartFile> files){
        Post post = new Post(userId,null,userName,null);
        for (MultipartFile file : files) {
            Map<String, String> contentDisposition = file.getContentDisposition();
            if (contentDisposition.get("name").equals("image")) {
                String filename = contentDisposition.get("filename");
                if (filename==null) continue;
                int lastPoint = filename.lastIndexOf('.');
                byte[] data = file.getData();
                UUID uuid = UUID.randomUUID();
                String storedFileName = uuid.toString() + filename.substring(lastPoint);
                post.setImage(storedFileName);
                try {
                    FileUtil.writeAllBytesToFile(data,storedFileName);
                } catch (IOException e) {
                    log.error("이미지 쓸 때 에러 발생");
                    throw new RuntimeException(e);
                }
            }
            else{
                byte[] data = file.getData();
                post.setContent(new String(data));
            }
        }
        return post;
    }
}

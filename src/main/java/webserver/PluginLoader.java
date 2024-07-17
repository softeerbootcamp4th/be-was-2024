package webserver;

import annotations.Get;
import annotations.Plugin;
import annotations.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * 어노테이션을 기반으로 메소드들를 플러그인 매퍼에 담는 클래스
 */
public class PluginLoader {
    public static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private final PluginMapper pluginMapper;

    public PluginLoader(PluginMapper pluginMapper){
        this.pluginMapper = pluginMapper;
    }

    /**
     * 특정 패키지에서 플러그인 클래스를 동적으로 로드하는 메소드
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public void loadPlugins() throws ClassNotFoundException, IOException, URISyntaxException {
        // 패키지 내 모든 클래스를 가져옴
        for (Class<?> cls : getAllClasses()) {
            // Plugin 인터페이스를 구현한 클래스인 경우만 추가
            if (cls.isAnnotationPresent(Plugin.class)) {
                // 인스턴스 생성 후 리스트에 추가
                for (Method method : cls.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Post.class)) {
                        Post post = method.getAnnotation(Post.class);
                        pluginMapper.put(HttpMethod.POST, post.path(), method);
                    }
                    if (method.isAnnotationPresent(Get.class)) {
                        Get get = method.getAnnotation(Get.class);
                        pluginMapper.put(HttpMethod.GET, get.path(), method);
                    }
                }
            }
        }
    }

    // 전체 클래스패스에서 모든 클래스를 가져오는 메소드
    private List<Class<?>> getAllClasses() throws IOException, ClassNotFoundException, URISyntaxException {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.toURI());
            if (directory.exists() && directory.isDirectory()) {
                classes.addAll(findClasses(directory, ""));
            }
        }
        return classes;
    }

    // 디렉토리를 탐색하여 클래스 파일을 찾는 메소드
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (packageName.isEmpty()) {
                        classes.addAll(findClasses(file, file.getName()));
                    } else {
                        classes.addAll(findClasses(file, packageName + "." + file.getName()));
                    }
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    if (className.startsWith(".")) {
                        className = className.substring(1);
                    }
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }
}

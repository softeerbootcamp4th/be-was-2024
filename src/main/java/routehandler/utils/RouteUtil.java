package routehandler.utils;

import java.util.Arrays;

public class RouteUtil {
    // path variable인지 여부를 반환
    public static boolean isPathVariable(String pathSegment) {
        return pathSegment.startsWith("{") && pathSegment.endsWith("}");
    }

    public static String getPathVariable(String pathSegment) {
        if(!isPathVariable(pathSegment)) throw new RuntimeException("no path variable path = " + pathSegment);

        return pathSegment.substring(1, pathSegment.length() - 1);
    }

    public static String[] getPathSegments(String pathname){
        String[] segments = pathname.split("/");
        // 슬래시로 시작하는 경로 => 첫번째 요소는 "". 첫번째 요소를 제거하고 내보낸다.
        // "/" 경로는 split 하면 어떤 값도 나오지 않는다.
        if(segments.length == 0) return new String[] {""};
        if(pathname.startsWith("/")) return Arrays.copyOfRange(segments, 1, segments.length);
        return segments;
    }

    public static String getPathname(String... pathSegments) {
        StringBuilder pathnameBuilder = new StringBuilder();

        for(String pathSegment : pathSegments) {
            if(pathSegment == null) continue;
            if(!pathSegment.startsWith("/")) pathnameBuilder.append("/");
            pathnameBuilder.append(pathSegment);
        }

        return pathnameBuilder.toString();
    }
}
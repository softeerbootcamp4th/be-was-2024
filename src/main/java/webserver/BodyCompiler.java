package webserver;

import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyCompiler {
    private final static String TRIM = "[\\r\\n\\s]*";
    private final static String STATEMENT = "(.*?)";
    private final static String LAST_STATEMENT = "(.*)";

    public static String compile(HashMap<String, String> param, String body) {
        Pattern pattern = Pattern.compile("\\{%" + TRIM + "(.*?)" + TRIM + "%}", Pattern.DOTALL); // {% syntax %} 패턴

        Matcher matcher = pattern.matcher(body);

        return matcher.replaceAll(matchResult -> replace(param, matchResult));
    }

    // s1: boolean
    private static String replace(HashMap<String, String> param, MatchResult matchResult) {
        String content = matchResult.group(1);

        // s1 ? s2 : s3
        Pattern TernaryOpPattern = Pattern.compile(STATEMENT + TRIM + "\\?" + TRIM + STATEMENT + TRIM + ":" + TRIM + LAST_STATEMENT, Pattern.DOTALL);
        Matcher matcher = TernaryOpPattern.matcher(content);
        if (matcher.find()) return isStatementTrue(param, matcher.group(1)) ? replaceVarToValue(param, matcher.group(2))
                                                                            : replaceVarToValue(param, matcher.group(3));

        // s1 && s2
        Pattern AndOpPattern = Pattern.compile(STATEMENT + TRIM + "&&" + TRIM + LAST_STATEMENT, Pattern.DOTALL);
        matcher = AndOpPattern.matcher(content);
        if (matcher.find()) return isStatementTrue(param, matcher.group(1)) ? replaceVarToValue(param, matcher.group(2)) : "";

        // var
        String getVar = param.get(content);
        return getVar != null ? getVar : "";
    }

    private static String replaceVarToValue(HashMap<String, String> param, String content) {
        Pattern varPattern = Pattern.compile("\\{\\{" + TRIM + STATEMENT + TRIM + "}}", Pattern.DOTALL);
        Matcher matcher = varPattern.matcher(content);
        return matcher.replaceAll(matchResult -> getValueByHashMap(param, matchResult));
    }

    private static String getValueByHashMap(HashMap<String, String> param, MatchResult matchResult) {
        String getVar = param.get(matchResult.group(1));
        return getVar != null ? getVar : "";
    }

    public static boolean isStatementTrue(HashMap<String, String> param, String statement) {
        String getVar = param.get(statement);
        return getVar != null && getVar.equals("true");
    }
}

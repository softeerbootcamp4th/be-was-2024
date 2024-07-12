package view;

public class TemplateRegex {
    public final static String block = "<\\s*my-template(\\s*|(\\s+[^>]+))\\s*>(.*?)</\\s*my-template\\s*>";
    public final static String attribute = "([\\w-]+)\\s*=\\s*(\"[^\"]*\"|'[^']*')";
    public final static String property = "\\{\\s*([\\w\\.]+)\\s*\\}";
}

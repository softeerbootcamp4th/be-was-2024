package view;

public class TemplateRegex {
    public final static String block = "<\\s*my-template(\\s*|(\\s+[^>]+))\\s*>(.*?)</\\s*my-template\\s*>";
    public final static String attribute = "([\\w-_@\\.]+)\\s*=\\s*(\"[^\"]*\"|'[^']*')";
    public final static String property = "\\{\\s*([\\w-_@\\.]+)\\s*\\}";
}

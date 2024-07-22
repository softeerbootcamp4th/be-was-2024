package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class View {
    private final String template;

    public View(String templatePath) throws IOException {
        this.template = readFileToString(templatePath);
    }

    private String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    public String render(Map<String, Object> model) {
        String renderedHtml = template;
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            renderedHtml = renderedHtml.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
        }
        return renderedHtml;
    }
}
package model;

import utils.PathUtils;
import utils.ResourcesLoader;
import utils.Template;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
  private final String view;
  private final Map<String, Object> model;

  public ModelView(String view, Map<String, Object> model) {
    this.view = view;
    this.model = model;
  }

  public void addAttribute(String key, Object value) {
    model.put(key, value);
  }

  public byte[] render() throws IllegalAccessException {
    String viewName = PathUtils.filePathResolver(view);
    byte[] file = ResourcesLoader.getFile(viewName);
    if(viewName.endsWith(".html")) {
      return Template.render(file, model);
    } else {
      return file;
    }
  }
}

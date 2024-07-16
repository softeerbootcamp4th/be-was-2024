package webserver;

import apiprocess.ApiProcess;

import enums.MimeType;
import model.ModelView;
import model.User;
import utils.AuthUtil;
import utils.PathUtils;

import java.util.HashMap;
import java.util.Map;

public class CommonApiProcess {

  private static class LazyHolder {
    private static final CommonApiProcess INSTANCE = new CommonApiProcess();
  }

  public static CommonApiProcess getInstance() {
    return LazyHolder.INSTANCE;
  }

  public ModelView getView(Request request, Response response) {
    Map<String, Object> model = new HashMap<>();

    User user = AuthUtil.isLogin(request);

    if(user != null) {
      model.put("user", user);
    }

    ApiProcessManager apiProcessManager = ApiProcessManager.getInstance();
    ApiProcess apiProcess = apiProcessManager.get(request.getPath(), request.getMethod());
    String fileNameWithoutExt = apiProcess.process(request, response, model);

    if(fileNameWithoutExt == null) return null;

    String fileName = PathUtils.filePathResolver(fileNameWithoutExt);

    MimeType mimeType = PathUtils.extToMimetype(fileName);
    response.setContentType(mimeType);

    return new ModelView(fileName, model);
  }
}

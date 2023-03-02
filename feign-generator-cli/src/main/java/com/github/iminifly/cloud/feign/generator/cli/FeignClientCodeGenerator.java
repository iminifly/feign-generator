package com.github.iminifly.cloud.feign.generator.cli;

import freemarker.template.TemplateException;

import static com.github.iminifly.cloud.plugin.feign.core.FeignClassAndModelResolver.resolveFeignClientClassList;
import static com.github.iminifly.cloud.plugin.feign.core.FeignClassAndModelResolver.resolveFeignModelList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.iminifly.cloud.plugin.feign.core.FeignClientScanner;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModelField;
import com.github.iminifly.cloud.plugin.feign.core.util.DynamicClasspath;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil.Text;

/**
 * FeignClientCodeGenerator
 *
 * @author XGF
 * @date 2020/11/12 22:26
 */
public class FeignClientCodeGenerator {

  static String server = "http://localhost:10001/api/v1/feign/generate";

  static boolean remote = false;

  private static final String USER_DIR = System.getProperty("user.dir");

  public static void main(String[] args) throws Exception {

    String packageName = args[1];

    String modelPackage = args[2];

    if (args.length >= 4 && "remote".equals(args[3])) {
      remote = true;
    }

    if (args.length >= 5 && args[4].length() > 0) {
      server = args[4];
    }

    DynamicClasspath dynamicClasspath = dynamicClasspath();

    FeignClientScanner scanner = new FeignClientScanner(modelPackage, dynamicClasspath);

    List<FeignClientClass> list = scanner.scan(packageName);

    FeignClassAndModel feignClassAndModel = new FeignClassAndModel();
    feignClassAndModel.setFeignClientClasses(list);

    List<FeignModel> models = new ArrayList<>();

    for (String modelClassName : scanner.getModelClassNames()) {

      try {

        Class<?> clazz = Class.forName(modelClassName, false, dynamicClasspath.getClassLoader());

        FeignModel model = new FeignModel();
        model.setPackageName(modelClassName.substring(0, modelClassName.lastIndexOf(".")));
        model.setModelName(modelClassName.substring(modelClassName.lastIndexOf(".") + 1));

        Field[] fields = clazz.getDeclaredFields();
        Field[] parentFields = null;
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass.getName().startsWith(modelPackage)) {
          parentFields = superclass.getDeclaredFields();
        }

        List<FeignModelField> feignModelFields = new ArrayList<>();

        for (Field field : fields) {
          FeignModelField modelField = new FeignModelField();
          modelField.setName(field.getName());
          modelField.setType(field.getGenericType().getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));
          feignModelFields.add(modelField);
        }
        if (parentFields != null) {
          for (Field field : parentFields) {
            FeignModelField modelField = new FeignModelField();
            modelField.setName(field.getName());
            modelField.setType(field.getGenericType().getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));
            feignModelFields.add(modelField);
          }
        }

        model.setModelFields(feignModelFields);

        models.add(model);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

    feignClassAndModel.setFeignModels(models);

    saveFeignClassAndModel(feignClassAndModel);

    if (remote) {
      String groupId = getGroupId();
      String serviceName = getServiceName();
      String version = getVersion();

      feignClassAndModel.setGroupId(groupId);
      feignClassAndModel.setVersion(version);
      feignClassAndModel.setServiceName(serviceName);

      FeignClassSendUtil.serializeAndSend(feignClassAndModel);
    }
  }

  private static DynamicClasspath dynamicClasspath() throws Exception {
    DynamicClasspath dynamicClasspath = DynamicClasspath.getInstance();
    String dir = System.getProperty("user.dir");
    dynamicClasspath.loadLib(dir + "/BOOT-INF/lib");
    dynamicClasspath.loadDir(dir + "/BOOT-INF/classes");
    return dynamicClasspath;
  }

  private static String getVersion() {
    return getProperties().getProperty("version");
  }

  private static String getServiceName() {
    return getProperties().getProperty("artifactId");
  }

  private static String getGroupId() {
    return getProperties().getProperty("groupId");
  }

  private static Properties getProperties() {
    File file = getPomProperties();
    if (file == null) {
      throw new RuntimeException("未找到META-INF/maven/**/pom.properties文件，无法获取工程信息");
    }
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(file));
    } catch (IOException ignored) {
    }
    return properties;
  }

  private static File getPomProperties() {

    String mavenDirName = USER_DIR + "/META-INF/maven";
    File mavenDir = new File(mavenDirName);

    List<File> files = new ArrayList<>();

    if (mavenDir.exists() && mavenDir.isDirectory()) {
      pomProperties(mavenDir, files);
      if (files.size() == 1) {
        return files.get(0);
      }
    }

    return null;
  }

  private static void pomProperties(File dir, List<File> files) {
    File[] listFiles = dir.listFiles();
    for (File file : listFiles) {
      if (file.isFile()) {
        if ("pom.properties".equals(file.getName())) {
          files.add(file);
        }
      } else {
        pomProperties(file, files);
      }
    }
  }

  private static void saveFeignClassAndModel(FeignClassAndModel feignClassAndModel)
      throws IOException, TemplateException {

    List<FeignClientClass> feignClientClasses = feignClassAndModel.getFeignClientClasses();

    List<FeignModel> feignModels = feignClassAndModel.getFeignModels();

    List<Text> feignClientClassList = resolveFeignClientClassList(feignClientClasses);
    List<Text> feignModelList = resolveFeignModelList(feignModels);

    makeFeignDir();

    for (Text text : feignClientClassList) {
      saveText(text);
    }

    for (Text text : feignModelList) {
      saveText(text);
    }
  }

  private static void makeFeignDir() {
    File dir = new File(USER_DIR + "/feign");
    dir.mkdir();
  }

  private static void saveText(Text text) {

    String fullFileName = text.getFileName();

    String dirName = "feign";
    String fileName = fullFileName;

    if (fullFileName.contains("/")) {
      dirName = dirName + "/" + fullFileName.substring(0, fullFileName.lastIndexOf("/"));
      fileName = fullFileName.substring(fullFileName.lastIndexOf("/"));
    }

    String fullDirName = USER_DIR + "/" + dirName;

    File dir = new File(fullDirName);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dirName + "/" + fileName);

    System.out.printf("Save file %s to %s\n", text.getFileName(), file.getAbsolutePath());

    try (OutputStream out = new FileOutputStream(file)) {
      out.write(text.getText().getBytes("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

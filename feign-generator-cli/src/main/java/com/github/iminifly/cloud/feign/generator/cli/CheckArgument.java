package com.github.iminifly.cloud.feign.generator.cli;

import java.io.File;

/**
 * CheckArgument
 *
 * @author XGF
 * @date 2020/11/22 19:02
 */
public class CheckArgument {

  private static final String PACKAGE_PATTERN = "^[a-z0-9]+(\\.[a-z0-9]+)*$";

  public static void main(String[] args) {

    // 验证三个参数
    if (args.length < 3) {
      System.out.println("Usage: run.bat jarFilePath controllerScanPath modelScanPath");
      System.exit(1);
    }

    // 验证第一个参数以.jar结尾
    if (!args[0].endsWith(".jar")) {
      System.out.printf("jarFilePath不是一个jar文件: %s\n", args[0]);
      System.exit(1);
    }

    File jarFile = new File(args[0]);

    // 验证jar文件存在
    if (!jarFile.exists()) {
      System.out.printf("jarFilePath不存在: %s\n", args[0]);
      System.exit(1);
    }

    // 验证jar文件确实是一个文件
    if (!jarFile.isFile()) {
      System.out.printf("jarFilePath不是一个文件: %s\n", args[0]);
      System.exit(1);
    }

    // 验证包名
    if (!args[1].matches(PACKAGE_PATTERN)) {
      System.out.printf("controllerScanPath不正确: %s\n", args[1]);
      System.exit(1);
    }
    if (!args[2].matches(PACKAGE_PATTERN)) {
      System.out.printf("modelScanPath不正确: %s\n", args[2]);
      System.exit(1);
    }

    System.exit(0);
  }
}

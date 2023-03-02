package com.github.iminifly.cloud.feign.generator.cli;

import com.alibaba.fastjson.JSON;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * FeignClassSendUtil
 *
 * @author xuguofeng
 * @date 2020/11/24 9:05
 */
class FeignClassSendUtil {

  static void serializeAndSend(FeignClassAndModel feignClassAndModel) throws IOException {

    System.out.println("Start send feign class and model metadata");

    String jsonString = JSON.toJSONString(feignClassAndModel);

    Map<String, String> headers = new HashMap<>(1);

    String response = HttpUtil.request(FeignClientCodeGenerator.server, jsonString, headers);

    System.out.println("Send feign client metadata response: " + response);
  }
}

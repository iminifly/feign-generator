package com.github.iminifly.cloud.plugin.feign.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HttpUtil
 *
 * @author XGF
 * @date 2020/12/6 20:20
 */
public class HttpUtil {

	public static String request(String requestUrl, String body, Map<String, String> headers) throws IOException {

		HttpURLConnection connection = null;
		OutputStream out = null;
		BufferedReader reader = null;

		try {

			URL url = new URL(requestUrl);

			connection = (HttpURLConnection) url.openConnection();

			if (headers != null && !headers.isEmpty()) {
				Set<Entry<String, String>> entries = headers.entrySet();
				for (Entry<String, String> entry : entries) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);

			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setDoOutput(true);

			out = connection.getOutputStream();
			out.write(body.getBytes("UTF-8"));
			out.close();

			int code = connection.getResponseCode();

			if (code != 200) {
				System.out.println("Send request " + requestUrl + " fail: " + code);
			} else {
				System.out.println("Send request " + requestUrl + " successfully");
			}

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line = reader.readLine();

			System.out.println("Http response: " + line);

			return line;

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ignored) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}

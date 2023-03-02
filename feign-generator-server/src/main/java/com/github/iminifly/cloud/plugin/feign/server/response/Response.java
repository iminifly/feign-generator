package com.github.iminifly.cloud.plugin.feign.server.response;

/**
 * Response
 *
 * @author xuguofeng
 * @date 2020/12/2 16:28
 */
public class Response<T> {

	private String code;

	private long responseTime;

	private T body;

	public Response() {
		this.responseTime = System.currentTimeMillis();
	}

	public static <T> Response<T> success(T body) {
		Response<T> response = new Response<>();
		response.setCode("000000");
		response.setBody(body);
		return response;
	}

	public static <T> Response<T> error(T body, String code) {
		Response<T> response = new Response<>();
		response.setCode(code);
		response.setBody(body);
		return response;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}

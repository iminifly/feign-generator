package com.github.iminifly.cloud.feign.demo.response;

import static com.github.iminifly.cloud.feign.demo.response.ResponseCodeEnum.ERROR;
import static com.github.iminifly.cloud.feign.demo.response.ResponseCodeEnum.OK;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 响应
 *
 * @author XGF
 * @date 2020/2/20 18:53
 */
@Data
public class Response<T> {

	private String code;

	private String message;

	private long timestamp;

	private T body;

	private Map<String, Object> data;

	public Response() {
		this.timestamp = System.currentTimeMillis();
	}

	public Response(String code, String message) {
		this();
		this.code = code;
		this.message = message;
	}

	public Response<?> add(String key, Object data) {
		if (this.data == null) {
			this.data = new HashMap<>(16);
		}
		this.data.put(key, data);
		return this;
	}

	public static <T> Response<T> success(T body) {
		return generateResponse(body, OK.getCode(), OK.getMessage());
	}

	public static <T> Response<T> error(T body) {
		return generateResponse(body, ERROR.getCode(), ERROR.getMessage());
	}

	public static <T> Response<T> generateResponse(T body, String code, String message) {
		Response<T> response = new Response<>(code, message);
		response.setBody(body);
		return response;
	}
}

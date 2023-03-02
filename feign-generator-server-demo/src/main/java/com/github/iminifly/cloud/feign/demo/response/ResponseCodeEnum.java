package com.github.iminifly.cloud.feign.demo.response;

/**
 * 响应状态码
 *
 * @author XGF
 * @date 2020/2/20 18:53
 */
public enum ResponseCodeEnum {

	/**
	 * 成功
	 */
	OK("000000", "OK"),

	/**
	 * 默认异常
	 */
	ERROR("000999", "system error");

	private String code;

	private String message;

	ResponseCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}

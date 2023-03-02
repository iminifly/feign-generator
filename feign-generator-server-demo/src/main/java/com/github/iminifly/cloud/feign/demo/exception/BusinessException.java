package com.github.iminifly.cloud.feign.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author XGF
 * @date 2020/3/1 18:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 5984953573536067856L;

	private String code;

	private String errorMessage;

	@Override
	public String getMessage() {
		return this.errorMessage;
	}

	@Override
	public String getLocalizedMessage() {
		return this.errorMessage;
	}
}

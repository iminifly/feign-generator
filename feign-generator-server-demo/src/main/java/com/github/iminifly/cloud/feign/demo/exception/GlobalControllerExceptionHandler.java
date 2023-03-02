package com.github.iminifly.cloud.feign.demo.exception;

import static com.github.iminifly.cloud.feign.demo.response.ResponseCodeEnum.ERROR;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.github.iminifly.cloud.feign.demo.response.Response;

/**
 * 全局异常处理类
 *
 * @author xgf
 * @date 2019/5/23
 */
@Slf4j
@RestControllerAdvice(annotations = { RestController.class })
public class GlobalControllerExceptionHandler {

	/**
	 * 业务全局异常处理函数
	 *
	 * @param e 异常
	 * @return 系统封装返回结构
	 */
	@ExceptionHandler(value = BusinessException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Response<String> handleException(BusinessException e) {
		log.error("catch exception:{}", e);
		return Response.generateResponse(null, e.getCode(), e.getErrorMessage());
	}

	/**
	 * 全局DTO校验异常处理, 增加Validated全局参数校验异常处理
	 *
	 * @param e 校验异常
	 * @return 返回系统统一结构
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Response<String> handleException(MethodArgumentNotValidException e) {
		log.error("catch exception:{}", e.getMessage());

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("method argument not valid exception:");

		BindingResult result = e.getBindingResult();
		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			errors.forEach(p -> {
				FieldError fieldError = (FieldError) p;
				log.error("Data check failure, object: {}, field: {}, error message: {}", fieldError.getObjectName(),
						fieldError.getField(), fieldError.getDefaultMessage());
				strBuf.append(",").append("\"Data check failure, object: ").append(fieldError.getObjectName())
						.append(", field: ").append(fieldError.getField()).append(", error message: ")
						.append(fieldError.getDefaultMessage());
			});
		}

		return Response.generateResponse(strBuf.toString(), ERROR.getCode(), ERROR.getMessage());
	}

	/**
	 * controller全局传参校验异常处理函数
	 *
	 * @param e 参数异常
	 * @return 返回系统统一结构
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<String> errorParam(ConstraintViolationException e) {
		log.error("catch exception:{}", e.getMessage());

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("method argument not valid exception:");

		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		if (constraintViolations.size() > 0) {
			constraintViolations.forEach(p -> {
				final String fieldName = ((PathImpl) p.getPropertyPath()).getLeafNode().getName();
				log.error("Data check failure, field: {}, value: {}, error message: {}", fieldName, p.getInvalidValue(),
						p.getMessage());
				strBuf.append(",").append("\"Data check failure, field: ").append(fieldName).append(", value: ")
						.append(p.getInvalidValue()).append(", error message: ").append(p.getMessage());
			});
		}

		return Response.generateResponse(strBuf.toString(), ERROR.getCode(), ERROR.getMessage());
	}

	/**
	 * 全局请求参数绑定异常处理
	 *
	 * @param e 转换异常
	 * @return 返回系统统一结构
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<String> paramsBindError(BindException e) {
		log.error("catch exception:{}", e.getMessage());

		StringBuffer strBuf = new StringBuffer();

		List<FieldError> fieldErrors = e.getFieldErrors();

		if (fieldErrors.size() > 0) {
			fieldErrors.forEach(p -> {
				final String fieldName = p.getField();
				log.error("Request parameter bind error, field: {}, value: {}, error message: {}", fieldName,
						p.getRejectedValue(), p.getDefaultMessage());
				strBuf.append(p.getField()).append(":").append(p.getDefaultMessage()).append(";");
			});
		}

		return Response.generateResponse(strBuf.toString(), ERROR.getCode(), ERROR.getMessage());
	}

	/**
	 * 增加全局参数转换异常处理
	 *
	 * @param e 转换异常
	 * @return 返回系统统一结构
	 */
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Response<String> handleException(MethodArgumentTypeMismatchException e) {
		log.error("catch exception:{}", e.getMessage());
		return Response.generateResponse(e.getLocalizedMessage(), ERROR.getCode(), ERROR.getMessage());
	}

	/**
	 * 增加全局Exception处理
	 *
	 * @param e 转换异常
	 * @return 返回系统统一结构
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Response<String> handleException(Exception e) {
		log.error("catch exception:{}", e.getMessage());
		return Response.generateResponse(e.getLocalizedMessage(), ERROR.getCode(), ERROR.getMessage());
	}
}

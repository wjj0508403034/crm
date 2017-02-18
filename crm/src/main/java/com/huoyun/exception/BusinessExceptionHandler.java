package com.huoyun.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.locale.LocaleService;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionHandler.class);

	@Autowired
	private LocaleService localeService;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> UnexpectedError(Exception exception) {
		logger.error(exception);
		logger.error(ExceptionUtils.getStackTrace(exception));
		return this.BusinessError(new BusinessException(BoErrorCode.System_Error));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> BusinessError(BusinessException businessException) {
		businessException.setMessage(this.localeService.getErrorMessage(businessException.getCode()));
		LOGGER.error("Internal System Error.", businessException);
		logger.error(ExceptionUtils.getStackTrace(businessException));
		return new ResponseEntity<Object>(new BusinessExceptionResponse(businessException), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LocatableBusinessException.class)
	public ResponseEntity<Object> LocatableBusinessError(LocatableBusinessException businessException) {
		LOGGER.error("Internal System Error.", businessException);
		logger.error(ExceptionUtils.getStackTrace(businessException));
		return new ResponseEntity<Object>(new LocatableBusinessExceptionResponse(businessException),
				HttpStatus.BAD_REQUEST);
	}
}

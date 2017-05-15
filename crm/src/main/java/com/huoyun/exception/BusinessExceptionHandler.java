package com.huoyun.exception;

import javax.persistence.RollbackException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.transaction.TransactionSystemException;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.locale.LocaleService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionHandler.class);

	@Autowired
	private LocaleService localeService;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> UnexpectedError(Exception exception) {
		logger.error(exception);
		return this.BusinessError(new BusinessException(BoErrorCode.System_Error));
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<Object> transactionSystemException(Exception exception) {
		logger.error(exception);
		if (exception.getCause() != null) {
			if (exception.getCause() instanceof RollbackException) {
				if (exception.getCause().getCause() != null
						&& exception.getCause().getCause() instanceof DatabaseException) {

					if (exception.getCause().getCause().getCause() != null) {
						if (exception.getCause().getCause()
								.getCause() instanceof MySQLIntegrityConstraintViolationException) {
							return this.BusinessError(
									new BusinessException(BoErrorCode.Delete_Failed_Due_To_Vaule_Is_Used));
						}
					}
				}
			}
		}

		return this.BusinessError(new BusinessException(BoErrorCode.System_Error));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> BusinessError(BusinessException businessException) {
		if (StringUtils.isEmpty(businessException.getMessage())) {
			businessException.setMessage(this.localeService.getErrorMessage(businessException.getCode()));
		}
		LOGGER.error("Internal System Error.", businessException);
		return new ResponseEntity<Object>(new BusinessExceptionResponse(businessException), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LocatableBusinessException.class)
	public ResponseEntity<Object> LocatableBusinessError(LocatableBusinessException businessException) {
		LOGGER.error("Internal System Error.", businessException);
		return new ResponseEntity<Object>(new LocatableBusinessExceptionResponse(businessException),
				HttpStatus.BAD_REQUEST);
	}
}

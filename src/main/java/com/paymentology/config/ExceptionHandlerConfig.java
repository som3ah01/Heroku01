package com.paymentology.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.paymentology.dto.ExceptionDTO;
import com.paymentology.exceptions.BadFileExecption;

@ControllerAdvice
public class ExceptionHandlerConfig {
	Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

	@ExceptionHandler(value = { BadFileExecption.class })
	public ResponseEntity<ExceptionDTO> handleResourceNotFoundException(BadFileExecption ex) {
		ExceptionDTO e = new ExceptionDTO(ex.getMessageKey(), ex.getMessage());
		logger.error(e.toString());
		return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	}

}

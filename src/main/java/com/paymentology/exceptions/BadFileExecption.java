package com.paymentology.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadFileExecption extends RuntimeException{

	private static final long serialVersionUID = -5789802716668090512L;
	private final String messageKey;

	public BadFileExecption(String message, String messageKey) {
		super(message);

		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

}

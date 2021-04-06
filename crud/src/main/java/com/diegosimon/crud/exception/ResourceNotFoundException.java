package com.diegosimon.crud.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException implements Serializable {

	/**
	 * SerialversionUID
	 */
	private static final long serialVersionUID = -8617993391604263416L;

	public ResourceNotFoundException(String exception) {
		super(exception);
	}
}

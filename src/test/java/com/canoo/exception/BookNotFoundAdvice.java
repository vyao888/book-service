package com.canoo.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.*;

@RestControllerAdvice
public class BookNotFoundAdvice {
	
	@ResponseBody
	@ExceptionHandler(BookNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String bookNotFoundHandler(final BookNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(NoBookUpdatedOrDeletedException.class)
	@ResponseStatus(HttpStatus.NOT_MODIFIED)
	public String noBookUpdatedOrDeletedHandler(final NoBookUpdatedOrDeletedException ex) {
		return ex.getMessage();
	}
}

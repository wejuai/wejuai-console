package com.wejuai.console.config;

import com.endofmaster.commons.util.validate.ValidateParamIsNullException;
import com.endofmaster.commons.util.validate.ValidateStringIsBlankException;
import com.endofmaster.rest.exceptionhandler.ErrorCode;
import com.endofmaster.rest.exceptionhandler.ErrorMessage;
import com.endofmaster.rest.exceptionhandler.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author YQ.Huang
 */
@RestControllerAdvice
class ErrorControllerAdvice extends RestExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleServerException(Exception e) {
        logger.error("Handle Exception", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.InternalServerError, e.getLocalizedMessage());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleException(ResourceAccessException e) {
        logger.error("Handle ResourceAccessException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.ServiceNotAvailable, "服务暂不可用，请稍后再试");
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> InterruptedException(InterruptedException e) {
        logger.error("Handle InterruptedException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.BadRequest, e.getLocalizedMessage());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidateStringIsBlankException.class)
    public ResponseEntity<Object> handleException(ValidateStringIsBlankException e) {
        logger.error("Handle ValidateStringIsBlankException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.BadRequest, e.getLocalizedMessage());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidateParamIsNullException.class)
    public ResponseEntity<Object> handleException(ValidateParamIsNullException e) {
        logger.error("Handle ValidateParamIsNullException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.BadRequest, e.getLocalizedMessage());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleException(HttpClientErrorException e) {
        logger.error("Handle HttpClientErrorException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.BadRequest, e.getResponseBodyAsString());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleException(HttpServerErrorException e) {
        logger.error("Handle HttpServerErrorException", e);
        ErrorMessage message = new ErrorMessage(ErrorCode.InternalServerError, e.getResponseBodyAsString());
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

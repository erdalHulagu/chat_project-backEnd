package com.sohbet.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sohbet.exception.message.ApiResponseError;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class FurkanChatExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FurkanChatExceptionHandler.class);

    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        logger.error("Error occurred: {}", error.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }

    private ApiResponseError createApiResponseError(HttpStatus status, String message, WebRequest request) {
        return new ApiResponseError(status, message, request.getDescription(false));
    }

    // Custom Exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.CONFLICT, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ResourceForbiddenException ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    // Security-related Exceptions
    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    protected ResponseEntity<Object> handleSecurityExceptions(Exception ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    // Spring Framework Exceptions
    
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
        ApiResponseError error = createApiResponseError(HttpStatus.BAD_REQUEST, String.join(", ", errors), request);
        return buildResponseEntity(error);
    }

    
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

  
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.BAD_REQUEST, "Malformed JSON request", request);
        return buildResponseEntity(error);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.BAD_REQUEST, "Missing request parameter: " + ex.getParameterName(), request);
        return buildResponseEntity(error);
    }

    // Generic RuntimeException and Exception
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        ApiResponseError error = createApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return buildResponseEntity(error);
    }
}


//@ControllerAdvice // merkezi exception handle etmek için @ControllerAdvice ekledim
//public class FurkanChatExceptionHandler  extends ResponseEntityExceptionHandler{
//	
//	// AMACIM : custom bir exception sistemini kurmak, gelebilecek exceptionları override ederek, istediğim yapıda
//	// cevap verilmesini sağlıyorum
//	
//	Logger logger = LoggerFactory.getLogger(FurkanChatExceptionHandler.class);
//	
//   private ResponseEntity<Object> buildResponseEntity(  ApiResponseError error) {
//	   logger.error(error.getMessage()); //  eception fırlarsa mesajını loggladık
//	   return new ResponseEntity<>(error, error.getStatus());
//	   
//   }
//	
//	
//	@ExceptionHandler(ResourceNotFoundException.class) // bu @ ile custom exceptionrımı yakalayacağım
//	protected ResponseEntity<Object> handleResourceNotFoundException ( ResourceNotFoundException ex, WebRequest  request  )   {
//																																																// WebRequest = gelen request
//			ApiResponseError error =  new ApiResponseError( HttpStatus.NOT_FOUND, 
//																														ex.getMessage(), 
//																														request.getDescription(false)  );
//	        
//			/* 
//			 *  Map<String,String> map= new HashMap<>();
//			 *  map.put("time", LocalDateTime.noe().toString());
//		     *  map.put("message", ex.getMessage());
//		     *  return new ResponseEntity<>(map,HttpStatus.CREATED);
//			 */
//				
//			
//			return buildResponseEntity(error);
//			
//	}
//	
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		
//		    List<String> errors = ex.getBindingResult().getFieldErrors(). // bütün field errorlarını get ile aldım
//		    																			stream().
//		    																			map(e->e.getDefaultMessage()).//bütün errorların getMessage() metodunu alıyorum
//		    																			collect(Collectors.toList());
//		    
//		    ApiResponseError error = new  ApiResponseError(HttpStatus.BAD_REQUEST,
//		    																				errors.get(0).toString(),
//		    																				request.getDescription(false));
//		    
//		    return buildResponseEntity(error);
//		    		
//	
//	}
//	
//	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
//			HttpStatus status, WebRequest request) {
//		      ApiResponseError error = new  ApiResponseError(HttpStatus.BAD_REQUEST,
//		    		  																					ex.getMessage(),
//		    		  																				     request.getDescription(false));
//		      return buildResponseEntity(error);
//	}
//	
//	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		ApiResponseError error = new  ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
//					ex.getMessage(),
//				     request.getDescription(false));
//        return buildResponseEntity(error);
//	}
//	
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		
//		ApiResponseError error = new  ApiResponseError(HttpStatus.BAD_REQUEST,
//				ex.getMessage(),
//			     request.getDescription(false));
//    return buildResponseEntity(error);
//		
//	}
//	
//	@ExceptionHandler(ConflictException.class)
//	protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
//		ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT, 
//																												ex.getMessage(), 
//																												request.getDescription(false));
//		return buildResponseEntity(error);
//	}
//	
//	
//	// Security ile ilgili Exceptionlar handle adiliyor
//	
//	@ExceptionHandler(AccessDeniedException.class)
//	protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//		ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN, 
//																												ex.getMessage(), 
//																												request.getDescription(false));
//		return buildResponseEntity(error);
//	}
//	
//	@ExceptionHandler(AuthenticationException.class)
//	protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
//		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, 
//																												ex.getMessage(), 
//																												request.getDescription(false));
//		return buildResponseEntity(error);
//	}
//	
//	
//	@ExceptionHandler(BadRequestException.class)
//	protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
//		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, 
//																												ex.getMessage(), 
//																												request.getDescription(false));
//		return buildResponseEntity(error);
//	}
//	
//	
//	@ExceptionHandler(RuntimeException.class)
//	protected ResponseEntity<Object> handleRuntimeException ( RuntimeException ex, WebRequest  request  )   {
//		
//		ApiResponseError error =  new ApiResponseError( HttpStatus.INTERNAL_SERVER_ERROR, 
//																									ex.getMessage(), 
//																									request.getDescription(false)  );
//
//		return buildResponseEntity(error);
//
//}
//	
//	@ExceptionHandler(Exception.class)
//	protected ResponseEntity<Object> handleGeneralException ( Exception ex, WebRequest  request  )   {
//		
//		ApiResponseError error =  new ApiResponseError( HttpStatus.INTERNAL_SERVER_ERROR, 
//																								ex.getMessage(), 
//																								request.getDescription(false)  );
//
//		return buildResponseEntity(error);
//
//}
//	@ExceptionHandler(ResourceForbiddenException.class)
//    public ResponseEntity<String> handleForbiddenException(ResourceForbiddenException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403 Forbidden: " + ex.getMessage());
//    }
//	
//
//
//}

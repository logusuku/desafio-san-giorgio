package br.com.desafio.domain.exception.handler;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<String> handleSellerNotFoundException(SellerNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<String> handleInvoiceNotFoundException(InvoiceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

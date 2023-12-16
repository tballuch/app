package com.interview.pdfApp.exception.handler;

import com.interview.pdfApp.exception.DownloadException;
import com.interview.pdfApp.exception.PdfException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PdfException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handlePdfException() {
        return new ResponseEntity<>(
                "An exception occurred while generating PDF, see the logs for more details.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DownloadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleDownloadException() {
        return new ResponseEntity<>(
                "An exception occurred while accessing PDF, see the logs for more details.",
                HttpStatus.NOT_FOUND
        );
    }
}
package com.interview.pdfApp.exception;

public class PdfException extends RuntimeException {
    public PdfException(){
        super("Error while creating Pdf");
    }
}

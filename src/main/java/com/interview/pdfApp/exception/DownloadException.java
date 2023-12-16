package com.interview.pdfApp.exception;

public class DownloadException extends RuntimeException {
    public DownloadException(){
        super("Error while downloading PDF");
    }
}

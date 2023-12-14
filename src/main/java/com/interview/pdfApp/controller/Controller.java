package com.interview.pdfApp.controller;


import com.interview.pdfApp.entity.Item;
import com.interview.pdfApp.repository.ItemRepository;
import com.interview.pdfApp.service.ItemService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class Controller {
    private final ItemService itemService;

    @GetMapping("/item/generate")
    ResponseEntity<String> generateItems() {
        itemService.generateItems();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Items generated successfully", headers, HttpStatus.OK);
    }

    @GetMapping("/pdf/{ids}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Set<Long> ids) throws DocumentException {
        ByteArrayOutputStream pdfStream = itemService.generatePdfStream(ids);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=items.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}

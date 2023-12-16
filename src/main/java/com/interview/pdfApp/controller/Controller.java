package com.interview.pdfApp.controller;


import com.interview.pdfApp.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@AllArgsConstructor
public class Controller {
    private final ItemService itemService;

    @Operation(summary = "Generate items and store them in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items successfully generated",
                    content = {@Content(mediaType = TEXT_PLAIN_VALUE)})})
    @PostMapping("/item/generate")
    ResponseEntity<String> generateItems() {
        itemService.generateItems();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Items generated successfully", headers, HttpStatus.OK);
    }

    @Operation(summary = "Generate a PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully generated and saved",
                    content = {@Content(mediaType = APPLICATION_PDF_VALUE)})})
    @GetMapping("/pdf/{ids}")
    public ResponseEntity<byte[]> exportPdf(
            @Parameter(description = "IDs of items to be included")
            @PathVariable Set<Long> ids) {
        ByteArrayOutputStream pdfStream = itemService.generatePdfStream(ids);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=items.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}

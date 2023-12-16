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

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@AllArgsConstructor
public class Controller {
    private final ItemService itemService;

    @Operation(summary = "Generate items and store them in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Items successfully created",
                    content = {@Content(mediaType = TEXT_PLAIN_VALUE)})})
    @PostMapping("/items/generate")
    ResponseEntity<String> generateItems() {
        itemService.generateItems();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Items generated successfully", headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Generate a PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully generated and saved",
                    content = {@Content(mediaType = TEXT_PLAIN_VALUE)})})
    @GetMapping("/pdf/{ids}")
    public ResponseEntity<String> generatePdf(
            @Parameter(description = "IDs of items to be included")
            @PathVariable Set<Long> ids) {
        String filename = itemService.generatePdfStream(ids);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Generated a file with name " + filename, headers, HttpStatus.OK);
    }

    @Operation(summary = "Download a PDF with a given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully downloaded",
                    content = {@Content(mediaType = APPLICATION_PDF_VALUE)})})
    @GetMapping("/file/{filename}")
    public ResponseEntity<byte[]> exportPdf(
            @Parameter(description = "Name of the file")
            @PathVariable String filename) {
        byte[] file = itemService.getFile(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.setContentLength(file.length);
        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
}

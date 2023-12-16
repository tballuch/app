package com.interview.pdfApp.service;

import com.interview.pdfApp.entity.Item;
import com.interview.pdfApp.exception.DownloadException;
import com.interview.pdfApp.exception.PdfException;
import com.interview.pdfApp.repository.ItemRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, BaseColor.BLACK);
    private final String fileDirPrefix = "pdf/";

    public void generateItems(){
        for (int i = 0; i < 20; i++) {
            int amount = (int) (Math.random() * 100);
            itemRepository.save(new Item("Item" + amount, amount));
        }
        log.info("Generation done");
    }

    public  String generatePdfStream(Set<Long> ids){

        List<Item> items = itemRepository.findAllById(ids);

        if (items.isEmpty()){
            log.error("No items found in the database for any given ID");
            throw new PdfException();
        } else if (items.size() < ids.size()){
            log.warn("Not all IDs found it the database");
        }

        ByteArrayOutputStream outputStream = createDocument(items);

        String filename = System.currentTimeMillis() / 1000 + ".pdf";
        try {
            new File(fileDirPrefix).mkdir();
            new File(fileDirPrefix + filename).createNewFile();
            FileOutputStream fos = new FileOutputStream(fileDirPrefix + filename);
            fos.write(outputStream.toByteArray());
        } catch (Exception e){
            log.error("Error while saving PDF: " + e);
            throw new PdfException();
        }
        return filename;
    }

    public byte[] getFile(String filename){
        try(FileInputStream input= new FileInputStream(fileDirPrefix + filename)) {
            return input.readAllBytes();
        }
        catch (Exception e) {
            log.error("Error while accessing object " + filename);
            throw new DownloadException();
        }
    }

    private ByteArrayOutputStream createDocument(List<Item> items){
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(2);
            table.addCell(new PdfPCell(new Phrase("Item Name",boldFont)));
            table.addCell(new PdfPCell(new Phrase("Amount",boldFont)));
            int sum = 0;
            for(Item item : items) {
                sum += item.getAmount();
                table.addCell(item.getName());
                table.addCell(String.valueOf(item.getAmount()));
            }
            table.addCell(new PdfPCell(new Phrase("Total amount",boldFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(sum),boldFont)));
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            log.error("Error while creating PDF: " + e);
            throw new PdfException();
        } finally {
            document.close();
        }
        return outputStream;
    }
}

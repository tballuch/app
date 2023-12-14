package com.interview.pdfApp.service;

import com.interview.pdfApp.entity.Item;
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
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, BaseColor.BLACK);

    public void generateItems(){
        for (int i = 0; i < 20; i++) {
            int amount = (int) (Math.random() * 100);
            itemRepository.save(new Item("Item" + amount, amount));
        }
        log.info("Generation done");
    }

    public  ByteArrayOutputStream generatePdfStream(Set<Long> ids){
        List<Item> items = itemRepository.findAllById(ids);
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
        } finally {
            document.close();
        }
        return outputStream;
    }

}

package com.testbot.testtaskbot.facade;

import com.testbot.testtaskbot.dto.UserForm;
import com.testbot.testtaskbot.service.MessageSender;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordReportFacade {

    private final MessageSender messageSender;

    public void generateReport(Long chatId, List<UserForm> userForms) {
        try (XWPFDocument document = new XWPFDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setFontSize(16);
            titleRun.setText("Отчет по удовлетворенности пользователей сервисом");

            XWPFTable table = document.createTable();

            XWPFTableRow headerRow = table.getRow(0);
            headerRow.getCell(0).setText("Имя");
            headerRow.addNewTableCell().setText("Email");
            headerRow.addNewTableCell().setText("Оценка");

            userForms.forEach(userForm -> {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(userForm.getName());
                row.getCell(1).setText(userForm.getEmail());
                row.getCell(2).setText(String.valueOf(userForm.getRate()));
            });

            document.write(out);

            sendFile(chatId, out);
        } catch (IOException e) {
            log.error("Got {} while generating word report", e.getMessage());
        }
    }

    private void sendFile(Long chatId, ByteArrayOutputStream out) {
        InputFile file = new InputFile();
        file.setMedia(new ByteArrayInputStream(out.toByteArray()), getFileName());
        messageSender.sendDocument(chatId, file);
    }

    private String getFileName() {
        String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm"));
        return "Отчет от %s.docx".formatted(localDateTime);
    }
}

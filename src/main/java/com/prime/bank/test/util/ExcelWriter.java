package com.prime.bank.test.util;

import com.prime.bank.test.enums.FileExtension;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Getter
@Setter
public class ExcelWriter {

    private static Logger logger = Logger.getLogger(ExcelWriter.class);

    private String fileName;
    private String sheetName;

    public static final String DECLARATION_BSN_NUMBER_PREFIX = "0";

    public <E, T> void writeXLSXFile(List<E> info, List<T> headerInfo) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(this.sheetName);

        prepareHeader(headerInfo, wb, sheet);
        prepareInfo(info, wb, sheet);

        prepareHttpEntity(headerInfo, wb, sheet, this.fileName);
    }


    protected <T> void prepareHttpEntity(List<T> headerInfo, XSSFWorkbook wb,
                                         XSSFSheet sheet, String fileName) {
        File downloadFolder = new File(Constants.DOWNLOAD_FOLDER_LOCATION);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
        try (FileOutputStream outByteStream = new FileOutputStream(new File(downloadFolder + "\\" + fileName))) {
            IntStream.range(0, headerInfo.size()).forEach(sheet::autoSizeColumn);

            wb.write(outByteStream);
        } catch (Exception ex) {
            logger.error("Unable to Export", ex);
        }
        return;
    }

    protected void prepareDefaultCellValue(List<String> cellValues, int cellIndex, XSSFCell cell) {
        if (Objects.nonNull(cellValues.get(cellIndex)) && !cellValues.get(cellIndex).isEmpty()
                && NumberUtils.isCreatable(cellValues.get(cellIndex))) {
            getNumericCellValue(cellValues.get(cellIndex), cell);
        } else {
            cell.setCellValue(Objects.nonNull(cellValues.get(cellIndex)) ? cellValues.get(cellIndex) : StringUtils.EMPTY);
        }
    }


    protected <T> void prepareHeader(List<T> headerInfo, XSSFWorkbook wb, XSSFSheet sheet) {
        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(getHeaderFont(wb));

        XSSFRow headerRow = sheet.createRow(0);
        for (int c = 0; c < headerInfo.size(); c++) {
            XSSFCell cell = headerRow.createCell(c);
            cell.setCellValue(String.valueOf(headerInfo.get(c)));
            cell.setCellStyle(headerCellStyle);
        }

        IntStream.range(0, headerInfo.size()).forEach(sheet::autoSizeColumn);
    }

    protected <E> void prepareInfo(List<E> info, XSSFWorkbook wb, XSSFSheet sheet) {
        CellStyle detailsCellStyle = wb.createCellStyle();
        detailsCellStyle.setFont(getDetailFont(wb));
        detailsCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        for (int r = 0; r < info.size(); r++) {
            XSSFRow row = sheet.createRow(r + 1);
            List<String> cellValues = (List<String>) info.get(r);
            for (int c = 0; c < cellValues.size(); c++) {
                XSSFCell cell = row.createCell(c);
                cell.setCellStyle(detailsCellStyle);
                prepareDefaultCellValue(cellValues, c, cell);
            }
        }
    }

    protected HttpHeaders getHttpHeader(int contentLength, String fileName) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType(FileExtension.getMimeType(fileName)));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(contentLength);
        return header;
    }

    protected Font getDetailFont(XSSFWorkbook wb) {
        Font detailsFont = wb.createFont();
        detailsFont.setFontHeightInPoints((short) 12);
        detailsFont.setFontName("calibri");
        return detailsFont;
    }

    protected Font getHeaderFont(XSSFWorkbook wb) {
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName("calibri");
        return headerFont;
    }

    protected void getNumericCellValue(String value, XSSFCell cell) {
        if (Objects.isNull(value) || Objects.equals(Double.parseDouble(value), 0.0)) {
            cell.setCellValue(StringUtils.EMPTY);
        } else {
            cell.setCellValue(NumberUtils.toLong(value, Long.parseLong(value)));
        }
    }
}

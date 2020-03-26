package com.prime.bank.test.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


public class ExcelReader {
    private final DataFormatter fmt = new DataFormatter();
    private Workbook wb;
    private int sheetNo;
    private Sheet sheet;

    public ExcelReader(Workbook wb) {
        this.wb = wb;
    }

    public static ExcelReader init(InputStream in) throws IOException, InvalidFormatException {
        return new ExcelReader(WorkbookFactory.create(in));

    }

    public Cell getCell(int row, int cell) {
        return getSheet().getRow(row).getCell(cell);
    }

    public String getStringValueOfCell(int row, int cellNo) {
        Cell cell = getCell(row, cellNo);
        if (Objects.nonNull(cell) && cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else {
            return null;
        }
    }


    public Integer getIntegerValueOfCell(int row, int cellNo) {
        Cell cell = getCell(row, cellNo);
        if (Objects.nonNull(cell) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else {
            return null;
        }
    }

    public ExcelReader selectSheet(int no) {
        this.sheetNo = no;
        this.sheet = null;
        return this;
    }

    public int getLastRowNum() {
        return getSheet().getLastRowNum();
    }

    public void forEachRow(Consumer<Integer> consumer) {
        for (int i = 0; i <= getLastRowNum(); i++) {
            consumer.accept(i);
        }

    }

    private Sheet getSheet() {
        if (sheet == null) {
            sheet = wb.getSheetAt(sheetNo);
        }
        return sheet;
    }
}

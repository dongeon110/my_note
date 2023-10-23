package com.note.dbdocs.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public abstract class DbdocsWorkbook implements Closeable {
    protected SXSSFWorkbook workbook;
    protected CellStyle mainTitleCellStyle;
    protected CellStyle subTitleCellStyle;
    protected CellStyle centerBorderCellStyle;
    protected CellStyle borderCellStyle;
    protected CellStyle blankCenterCellStyle;
    protected CellStyle blankCellStyle;

    /**
     * 생성자
     */
    public DbdocsWorkbook() {
        workbook = new SXSSFWorkbook();

        workbook.setCompressTempFiles(true);

        // 기본 셀 스타일
        mainTitleCellStyle = DbdocsCellStyleUtils.getMainTitleCellStyle(workbook);
        subTitleCellStyle = DbdocsCellStyleUtils.getSubTitleCellStyle(workbook);
        centerBorderCellStyle = DbdocsCellStyleUtils.getBorderCenterCellStyle(workbook);
        borderCellStyle = DbdocsCellStyleUtils.getBorderCellStyle(workbook);
        blankCenterCellStyle = DbdocsCellStyleUtils.getBlankCenterCellStyle(workbook);
        blankCellStyle = DbdocsCellStyleUtils.getBlankCellStyle(workbook);
    }

    /**
     * Sheet 생성
     * @param sheetName 시트명
     * @return SXSSFSheet
     */
    public SXSSFSheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * AutoClosable 메서드
     * @throws IOException
     */
    public void close() throws IOException {
        workbook.close();
    }

    /**
     * write
     * @param fos
     * @throws IOException
     */
    public void write(OutputStream fos) throws IOException {
        workbook.write(fos);
    }

    /**
     * 문자열이 공백인지 확인하는 메서드
     * @param str
     * @return true : null, "", 공백을 제거해도 비어있는 경우
     * <br> false : 문자열 있음
     */
    protected boolean isEmptyString(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * sheet header 추가
     * @param sheet
     * @param lastCol 병합할 마지막 Column Index
     */
    public void createSheetHeader(SXSSFSheet sheet, int lastCol) {
        String sheetName = sheet.getSheetName();

        SXSSFRow row = sheet.createRow(0);

        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(sheetName);
        cell.setCellStyle(mainTitleCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
    }
}

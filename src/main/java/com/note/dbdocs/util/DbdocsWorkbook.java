package com.note.dbdocs.util;

import com.note.dbdocs.dto.TableCommentDTO;
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
import java.util.Arrays;
import java.util.List;

/**
 * Dbdocs의 Workbook
 */
@Slf4j
public class DbdocsWorkbook implements Closeable {

    private SXSSFWorkbook workbook;
    private static CellStyle mainTitleCellStyle;
    private static CellStyle subTitleCellStyle;
    private static CellStyle centerBorderCellStyle;
    private static CellStyle borderCellStyle;
    private static CellStyle blankCenterCellStyle;
    private static CellStyle blankCellStyle;

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
    private boolean isEmptyString(String str) {
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
     */
    public void createSheetHeader(SXSSFSheet sheet) {
        String sheetName = sheet.getSheetName();

        SXSSFRow row = sheet.createRow(0);

        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(sheetName);
        cell.setCellStyle(mainTitleCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
    }


    /**
     * 테이블목록 Header 생성
     * @param sheet
     */
    public void craeteListTableHeader(SXSSFSheet sheet) {
        // 시트 명
        String systemName = sheet.getSheetName();

        // sheet의 마지막 row
        int lastRowNum = sheet.getLastRowNum();
        
        // 마지막 row 에서 한줄 띄우고 다음 줄 부터 테이블의 헤더 생성
        int tableHeaderRowNum = lastRowNum + 2;

        // Header Row 생성
        SXSSFRow tableHeaderRow = sheet.createRow(tableHeaderRowNum);

        // 시스템명 타이틀
        SXSSFCell systemNameTitleCell = tableHeaderRow.createCell(0);
        systemNameTitleCell.setCellValue(systemName);
        systemNameTitleCell.setCellStyle(subTitleCellStyle);
        
        // 시스템명 타이틀 병합
        SXSSFCell systemNameTitleMergedCell = tableHeaderRow.createCell(1);
        systemNameTitleMergedCell.setCellStyle(subTitleCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(tableHeaderRowNum, tableHeaderRowNum, 0, 1));

        // 시스템명 체크 공백이 아니면 true / 공백이면 false
        boolean hasSystemName = !isEmptyString(systemName);

        // 시스템명
        SXSSFCell systemNameCell = tableHeaderRow.createCell(2);
        SXSSFCell systemNameMergedCell = tableHeaderRow.createCell(3);
        if (hasSystemName) {
            systemNameCell.setCellValue(systemName);
            systemNameCell.setCellStyle(borderCellStyle);
            systemNameMergedCell.setCellStyle(borderCellStyle);
        } else {
            systemNameCell.setCellStyle(blankCellStyle);
            systemNameMergedCell.setCellStyle(blankCellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(tableHeaderRowNum, tableHeaderRowNum, 2, 3));
    }

    /**
     * 테이블 목록 타이틀 생성
     * @param sheet
     */
    public void createListTableTitle(SXSSFSheet sheet) {
        // 마지막 row 다음 줄
        int rowNum = sheet.getLastRowNum() + 1;

        // Row 생성
        SXSSFRow row = sheet.createRow(rowNum);

        // 제목
        List<String> subTitleNameList = Arrays.asList("No", "테이블영문명", "테이블한글명", "비고");
        int subTitleIndex = 0;
        for(String subTitleName : subTitleNameList) {
            SXSSFCell cell = row.createCell(subTitleIndex++);
            cell.setCellStyle(subTitleCellStyle);
            cell.setCellValue(subTitleName);
        }
    }

    /**
     * DB 테이블 리스트 정보 추가
     * @param sheet
     * @param dtos
     */
    public void createDataRows(SXSSFSheet sheet, List<TableCommentDTO> dtos) {
        // 마지막 row 다음 줄
        int rowNum = sheet.getLastRowNum() + 1;

        // Datas
        for(TableCommentDTO tableCommentDTO : dtos) {
            // No
            SXSSFRow dataRow = sheet.createRow(rowNum++);
            SXSSFCell noCell = dataRow.createCell(0);
            noCell.setCellValue(rowNum-4);
            noCell.setCellStyle(centerBorderCellStyle);

            // 테이블영문명
            SXSSFCell tableCell = dataRow.createCell(1);
            tableCell.setCellValue(tableCommentDTO.getTableName());
            tableCell.setCellStyle(borderCellStyle);

            // 테이블한글명 (테이블 comment)
            SXSSFCell commentCell = dataRow.createCell(2);
            String tableComment = tableCommentDTO.getTableComment();
            if (isEmptyString(tableComment)) {
                commentCell.setCellStyle(blankCellStyle);
            } else {
                commentCell.setCellValue(tableCommentDTO.getTableComment());
                commentCell.setCellStyle(borderCellStyle);
            }

            // 비고
            SXSSFCell blankCell = dataRow.createCell(3);
            blankCell.setCellStyle(borderCellStyle);
        }
    }

    /**
     * 테이블 목록 테이블 만들기
     * @param sheet 표 테이블을 추가할 sheet
     * @param dtos 테이블 코멘트 DTO List
     */
    public void createTableListTable(SXSSFSheet sheet, List<TableCommentDTO> dtos) {

        createSheetHeader(sheet);
        craeteListTableHeader(sheet);
        createListTableTitle(sheet);
        createDataRows(sheet, dtos);
    }
}

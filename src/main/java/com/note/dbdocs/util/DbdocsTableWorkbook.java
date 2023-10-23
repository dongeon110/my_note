package com.note.dbdocs.util;

import com.note.dbdocs.dto.TableCommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import java.util.Arrays;
import java.util.List;

/**
 * Dbdocs의 Table Workbook
 */
@Slf4j
public class DbdocsTableWorkbook extends DbdocsWorkbook {

    /**
     * 생성자
     */
    public DbdocsTableWorkbook() {
        super();
    }

    /**
     * 테이블목록 Header 생성
     * @param sheet
     */
    public void craeteListTableHeader(SXSSFSheet sheet) {
        // 시트 명
        String systemName = "시스템명 입력";

        // sheet의 마지막 row
        int lastRowNum = sheet.getLastRowNum();
        
        // 마지막 row 에서 한줄 띄우고 다음 줄 부터 테이블의 헤더 생성
        int tableHeaderRowNum = lastRowNum + 2;

        // Header Row 생성
        SXSSFRow tableHeaderRow = sheet.createRow(tableHeaderRowNum);

        // 시스템명 타이틀
        SXSSFCell systemNameTitleCell = tableHeaderRow.createCell(0);
        systemNameTitleCell.setCellValue("시스템명");
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

        createSheetHeader(sheet, 3);
        craeteListTableHeader(sheet);
        createListTableTitle(sheet);
        createDataRows(sheet, dtos);
    }
}

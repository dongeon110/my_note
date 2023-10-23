package com.note.dbdocs.util;

import com.note.dbdocs.dto.ColumnCommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DbdocsColumnWorkbook extends DbdocsWorkbook {

    /**
     * Constructor
     */
    public DbdocsColumnWorkbook() {
        super();
    }

    /**
     * 테이블 시트 생성
     * @param sheet         수정할 sheet
     * @param dtoLists      테이블별 분할 되지 않은 dto List
     * @param systemName    시스템 명
     * @param writeDate     작성일
     * @param writer        작성자
     */
    public void createTableSheet(
              SXSSFSheet sheet
            , List<ColumnCommentDTO> dtoLists
            , String systemName
            , String writeDate
            , String writer) {

        // 시트 Header 생성
        createSheetHeader(sheet, 9);

        // 테이블 별 분할
        ArrayList<ArrayList<ColumnCommentDTO>> orderedList = devideColumnCommentDTO(dtoLists);

        // 테이블 별 표 생성
        for(ArrayList<ColumnCommentDTO> dtos: orderedList) {
            createTable(sheet, dtos, systemName, writeDate, writer);
        }
    }

    /**
     * 테이블 만들기
     *
     * @param sheet         테이블 추가할 sheet
     * @param dtoList       컬럼 DTO List
     * @param systemName    시스템 명
     * @param writeDate     작성일
     * @param writer        작성자
     */
    protected void createTable(
              SXSSFSheet sheet
            , List<ColumnCommentDTO> dtoList
            , String systemName
            , String writeDate
            , String writer) {
        // 첫번째 DTO
        ColumnCommentDTO firstDto = dtoList.get(0);

        // 첫번째 DTO에서 테이블 id, comment 조회
        String tableId = firstDto.getTableName();
        String tableComment = firstDto.getTableComment();

        // 테이블 헤더 생성
        createTableFirstHeader(sheet, systemName, writeDate, writer);
        createTableSecondHeader(sheet, tableId, tableComment);
        createTableThirdHeader(sheet, tableComment);

        // 테이블 데이터
        createColumnDataHeader(sheet);
        createColumnData(sheet, dtoList);

        // footer 인덱스키 / 업무 규칙
        createIndexHeader(sheet);
        createIndexData(sheet);
        createWorkRule(sheet);
    }

    /**
     * 표 첫번째 Header
     *
     * @param sheet
     * @param systemName 시스템명
     * @param writeDate  작성일
     * @param writer     작성자
     */
    protected void createTableFirstHeader(SXSSFSheet sheet, String systemName, String writeDate, String writer) {
        // Create Row
        SXSSFRow row = createNextRow(sheet, 1);

        // 시스템명
        createDataSet(sheet, row, "시스템명", 2, systemName, 1, false);

        // 작성일
        createDataSet(sheet, row, "작성일", 1, writeDate, 4, false);

        // 작성자
        createDataSet(sheet, row, "작성자", 1, writer, 1, false);
    }

    /**
     * 표 두번째 Header
     *
     * @param sheet
     * @param tableId       테이블명
     * @param tableComment  테이블 코멘트
     */
    protected void createTableSecondHeader(SXSSFSheet sheet, String tableId, String tableComment) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        // 테이블 ID
        createDataSet(sheet, row, "테이블ID", 2, tableId, 1);

        // 테이블 명
        createDataSet(sheet, row, "테이블명", 1, tableId, 6);
    }

    /**
     * 표 세번쨰 Header
     *
     * @param sheet
     * @param tableComment  테이블 설명 (테이블 코멘트)
     */
    protected void createTableThirdHeader(SXSSFSheet sheet, String tableComment) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        // 테이블 ID
        createDataSet(sheet, row, "테이블설명", 2, tableComment, 8);
    }


    /**
     * 데이터 테이블 Header
     *
     * @param sheet
     */
    protected void createColumnDataHeader(SXSSFSheet sheet) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        createTitleCell(sheet, row, "No", 1);
        createTitleCell(sheet, row, "컬럼ID", 1);
        createTitleCell(sheet, row, "컬럼명", 1);
        createTitleCell(sheet, row, "타입", 1);
        createTitleCell(sheet, row, "길이", 1);
        createTitleCell(sheet, row, "NULL", 1);
        createTitleCell(sheet, row, "KEY", 1);
        createTitleCell(sheet, row, "CD NUM", 1);
        createTitleCell(sheet, row, "비고", 1);
        createTitleCell(sheet, row, "data default", 1);
    }

    /**
     * 데이터 테이블 값 List 추가
     *
     * @param sheet
     * @param dtoList
     */
    protected void createColumnData(SXSSFSheet sheet, List<ColumnCommentDTO> dtoList) {
        int numero = 1;

        for(ColumnCommentDTO dto: dtoList) {
            createColumnData(sheet, dto, numero++);
        }
    }

    /**
     * ColumnCommentDTO List 나누기
     *
     * @param dtoList       dtoList는 tableName으로 이미 정렬된 데이터이어야 함
     * @return 같은 tableName을 가진 것들 끼리 List로 묶어내고, 묶어낸 리스트를 가지고 있는 ArrayList
     */
    protected ArrayList<ArrayList<ColumnCommentDTO>> devideColumnCommentDTO(List<ColumnCommentDTO> dtoList) {
        // 결과를 담을 ArrayList
        ArrayList<ArrayList<ColumnCommentDTO>> result = new ArrayList<>();

        // 비교를 위한 이전 dto
        ColumnCommentDTO beforeDTO = new ColumnCommentDTO();
        // 같은 ArrayList
        ArrayList<ColumnCommentDTO> tableDTOList = new ArrayList<>();

        for(ColumnCommentDTO dto: dtoList) {
            // 첫번째 dto 예외
            if (beforeDTO.getTableName() == null) {
                beforeDTO = dto;
                tableDTOList.add(dto);
                continue;
            }

            // 테이블 명
            String beforeTableName = beforeDTO.getTableName();
            String dtoTableName = dto.getTableName();

            // 테이블 명이 다를 경우
            if (!beforeTableName.equals(dtoTableName)) {
                // 기존 테이블 데이텨 result에 추가
                ArrayList<ColumnCommentDTO> cloneTableDTOList = (ArrayList<ColumnCommentDTO>)tableDTOList.clone();
                result.add(cloneTableDTOList);

                // 새 테이블 데이터
                tableDTOList = new ArrayList<>();
            }

            // 테이블 데이터에 추가
            tableDTOList.add(dto);
            beforeDTO = dto; // 비교 dto 에 수정
        }

        // 마지막 추가
        result.add(tableDTOList);

        return result;
    }

    /**
     * 데이터 테이블 값 추가
     *
     * @param sheet             추가할 sheet
     * @param columnCommentDTO  컬럼 정보
     * @param num               numero sign
     */
    protected void createColumnData(SXSSFSheet sheet, ColumnCommentDTO columnCommentDTO, int num) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        createDataCell(sheet, row, String.valueOf(num), 1); // numero sign
        createDataCell(sheet, row, columnCommentDTO.getColumnName(), 1); // 컬럼ID
        createDataCell(sheet, row, columnCommentDTO.getColumnComment(), 1); // 컬럼명
        createDataCell(sheet, row, columnCommentDTO.getColumnType(), 1); // 타입
        createDataCell(sheet, row, String.valueOf(columnCommentDTO.getColumnLength()), 1, false); // 길이
        createDataCell(sheet, row, columnCommentDTO.getColumnIsNullable(), 1); // NULL
        createDataCell(sheet, row, columnCommentDTO.getPk(), 1, false); // KEY
        createDataCell(sheet, row, "", 1, false); // CD NUM
        createDataCell(sheet, row, "", 1, false); // 비고
        createDataCell(sheet, row, columnCommentDTO.getDefaultValue(), 1, false); // data default
    }

    /**
     * 인덱스 키 표 Header
     * @param sheet     추가할 sheet
     */
    protected void createIndexHeader(SXSSFSheet sheet) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        createTitleCell(sheet, row, "인덱스명", 2);
        createTitleCell(sheet, row, "인덱스키", 8);
    }

    /**
     * 인덱스 키 표 데이터
     * @param sheet
     */
    protected void createIndexData(SXSSFSheet sheet) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        createDataCell(sheet, row, "", 2, false);
        createDataCell(sheet, row, "", 8, false);
    }

    /**
     * 업무규칙
     */
    protected void createWorkRule(SXSSFSheet sheet) {
        // Create Row
        SXSSFRow row = createNextRow(sheet);

        createTitleCell(sheet, row, "업무규칙", 2);
        createDataCell(sheet, row, "", 8, false);
    }

    /**
     * 다음 Row를 얻는 메서드
     * @param sheet row를 만들 sheet
     * @param space 공백 개수
     * @return 생성된 Row
     */
    protected SXSSFRow createNextRow(SXSSFSheet sheet, int space) {
        int rowNum = sheet.getLastRowNum() + 1 + space;
        return sheet.createRow(rowNum);
    }

    /**
     * @see #createNextRow(SXSSFSheet, int) 
     */
    protected SXSSFRow createNextRow(SXSSFSheet sheet) {
        return createNextRow(sheet, 0);
    }

    /**
     * 제목과 값의 세트를 셀을 넣는 메서드
     *
     * @param sheet             편집할 Sheet
     * @param row               제목과 값을 넣을 Row, sheet 내의 row 이어야 함
     * @param title             제목 명
     * @param titleMergedCell   제목 셀 개수 (1: 1개, 2: 2개)
     * @param data              데이터 값
     * @param dataMergedCell    데이터 값 셀 개수
     * @param checkBlank        값이 없는 경우 데이터 값 셀 수정 여부 (true: 없으면 색 추가, false: 아무것도 안함)
     */
    protected void createDataSet(SXSSFSheet sheet, SXSSFRow row, String title, int titleMergedCell, String data, int dataMergedCell, boolean checkBlank) {
        createTitleCell(sheet, row, title, titleMergedCell);
        createDataCell(sheet, row, data, dataMergedCell, checkBlank);
    }

    /**
     * @see #createDataSet(SXSSFSheet, SXSSFRow, String, int, String, int, boolean)
     */
    protected void createDataSet(SXSSFSheet sheet, SXSSFRow row, String title, int titleMergedCell, String data, int dataMergedCell) {
        createDataSet(sheet, row, title, titleMergedCell, data, dataMergedCell, true);
    }

    /**
     * 제목이 있는 셀을 만드는 메서드
     *
     * @param sheet             편집할 Sheet
     * @param row               셀을 넣을 Row, sheet 내의 row 이여야 함
     * @param titleName         제목 명
     * @param mergedCellCount   병합 셀 개수
     */
    protected void createTitleCell(SXSSFSheet sheet, SXSSFRow row, String titleName, int mergedCellCount) {
        // row 다음 셀 위치
        int firstCol = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();

        // 병합 셀 값 수정
        mergedCellCount--;

        // 메인 셀 생성
        SXSSFCell cell = row.createCell(firstCol);
        cell.setCellValue(titleName);

        // 병합할 셀 이 있는 경우
        if (mergedCellCount > 0) {
            createMergedCells(row, subTitleCellStyle, firstCol, mergedCellCount);
        }

        // 병합
        if (mergedCellCount > 0) {
            int rowNum = sheet.getRowNum(row);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, firstCol, firstCol+mergedCellCount));
        }

        cell.setCellStyle(subTitleCellStyle);
    }

    /**
     * 데이터 셀 입력
     *
     * @param sheet             편집할 Sheet
     * @param row               값을 넣을 Row, Sheet 내의 row 이여야 함
     * @param data              데이터 값
     * @param mergedCellCount   병합 셀 개수
     * @param checkBlank        빈 값 체크. (true: 빈 값 스타일 변경, false: 변경 하지 않음)
     */
    protected void createDataCell(SXSSFSheet sheet, SXSSFRow row, String data, int mergedCellCount, boolean checkBlank) {
        // row 다음 셀 위치
        int firstCol = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();

        // 병합 셀 값 수정
        mergedCellCount--;

        // 데이터 셀 생성
        SXSSFCell cell = row.createCell(firstCol);

        CellStyle cellStyle;

        // 값 존재 여부에 따라 셀 스타일 다르게 적용
        if (isEmptyString(data) && checkBlank) {
            cellStyle = blankCenterCellStyle;
        } else {
            cell.setCellValue(data);
            cellStyle = centerBorderCellStyle;
        }
        cell.setCellStyle(cellStyle);
        
        // 병합할 셀이 있는 경우
        if (mergedCellCount > 0) {
            createMergedCells(row, cellStyle, firstCol, mergedCellCount);
        }

        // 병합
        if (mergedCellCount > 0) {
            int rowNum = sheet.getRowNum(row);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, firstCol, firstCol+mergedCellCount));
        }
    }

    /**
     * 병합할 셀을 미리 만드는 메서드
     * @param row
     * @param cellStyle
     * @param firstCol
     * @param mergedCellCount
     */
    protected void createMergedCells(SXSSFRow row, CellStyle cellStyle,  int firstCol, int mergedCellCount) {
        for (int idx=firstCol+1; idx<firstCol+mergedCellCount+1; idx++) {
            SXSSFCell mergedCell = row.createCell(idx);
            mergedCell.setCellStyle(cellStyle);
        }
    }

    /**
     * createDataCell boolean checkBlank 기본값 true
     *
     * @see #createDataCell(SXSSFSheet, SXSSFRow, String, int, boolean)
     */
    protected void createDataCell(SXSSFSheet sheet, SXSSFRow row, String data, int mergedCellCount) {
        createDataCell(sheet, row, data, mergedCellCount, true);
    }

}

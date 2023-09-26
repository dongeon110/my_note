package com.note.dbdocs.web;

import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.service.DbdocsPostgresService;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import com.note.utils.ResponseDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dbdocs/api")
public class DbDocsRestController {

    @Resource(name="dbdocsPostgresService")
    DbdocsPostgresService dbdocsPostgresService;

    /**
     * PostgreSQL DB의 테이블 이름과 코멘트를 조회
     * @param schemaName 스키마 명 (default="public")
     * @param tableName 테이블 명 (없는 경우 전체 조회)
     * @return
     */
    @GetMapping(value="/postgresTableComment")
    public ResponseEntity<?> getTableComment(
            @RequestParam(name="schema_name", defaultValue="public", required = false) String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName
    ) {

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .build();

        // 조회
        List<TableCommentDTO> tableCommentList = dbdocsPostgresService.selectTableCommentDTOList(dbdocsSrchInfo);
        
        // 조회 결과가 없는 경우
        if (tableCommentList.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(tableCommentList);
    }

    /**
     * PostgreSQL의 테이블 목록을 조회해서 엑셀로 다운로드
     * @param schemaName 스키마 명, 기본값 public
     * @param tableName 테이블 명, 없을 경우 전체
     * @param systemName 시스템 명, 없을 경우 제목 없음
     * @return
     */
    @GetMapping(value="/download/tableListExcel")
    public ResponseEntity<?> getDownloadTableList(
            @RequestParam(name="schema_name", defaultValue="public", required = false) String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName,
            @RequestParam(name="system_name", defaultValue="", required = false) String systemName
    ) {

        // 다운로드 되었을 떄 받을 파일 명 (확장자 미 포함)
        String downloadFilename = "PostgreSQL_TableList";

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .build();

        // 조회
        List<TableCommentDTO> tableCommentList = dbdocsPostgresService.selectTableCommentDTOList(dbdocsSrchInfo);

        try (
                SXSSFWorkbook workbook = new SXSSFWorkbook();
        ) {
            workbook.setCompressTempFiles(true);

            // Create Sheet
            SXSSFSheet sheet = workbook.createSheet("테이블목록");
            sheet.setRandomAccessWindowSize(100); // 메모리 행 100개로 제한, 초과시 Disk로 flush
            sheet.setColumnWidth(0, 32*70); // 32 * 1 (1 pixel)
            sheet.setColumnWidth(1, 32*430);
            sheet.setColumnWidth(2, 32*430);
            sheet.setColumnWidth(3, 32*150);

            /* TODO SOMETHING..
            *   1. Create Title Row
            *   2. Create Data Rows.
            *   3. Insert Data
            * */

            // 1. Create Title Row
            SXSSFRow titleRow = sheet.createRow(0);

            // 1.1 Title CellType
            CellType titleCellType = CellType.STRING;
            SXSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellType(titleCellType);
            titleCell.setCellValue("테이블목록");

            // 1.2 Main Title CellStyle
            CellStyle mainTitleCellStyle = workbook.createCellStyle();
            byte mainTitleColor[] = new byte[] {(byte) 242, (byte) 242, (byte) 242}; // 217, 217, 217
            mainTitleCellStyle.setFillForegroundColor(new XSSFColor(mainTitleColor, null));
            mainTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            mainTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            mainTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            titleCell.setCellStyle(mainTitleCellStyle);

            // 1.3 Merge Title Cells
            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:D1")); // Merged Title Cells

            // 2.0 Set SubTitle CellStyle
            CellStyle subTitleCellStyle = workbook.createCellStyle();
            // 2.0.1 Background Color
            byte subTitleColor[] = new byte[] {(byte) 217, (byte) 217, (byte) 217};
            subTitleCellStyle.setFillForegroundColor(new XSSFColor(subTitleColor, null));
            subTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 2.0.2 Border Line
            subTitleCellStyle.setBorderRight(BorderStyle.THIN);
            subTitleCellStyle.setBorderLeft(BorderStyle.THIN);
            subTitleCellStyle.setBorderTop(BorderStyle.THIN);
            subTitleCellStyle.setBorderBottom(BorderStyle.THIN);
            // 2.0.3 Sort
            subTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            subTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 2.0.4 System Title
            int titleRowNum = sheet.getLastRowNum();
            SXSSFRow systemNameRow = sheet.createRow(titleRowNum + 2);
            SXSSFCell systemNameTitleCell = systemNameRow.createCell(0);
            systemNameTitleCell.setCellValue("시스템명");
            systemNameTitleCell.setCellStyle(subTitleCellStyle);

            // 2.0.5 System Title Merge
            SXSSFCell systemNameTitleMergedCell = systemNameRow.createCell(1);
            systemNameTitleMergedCell.setCellStyle(subTitleCellStyle);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A3:B3")); // Merged Title Cells


            // 2.1.0 Set Data CellStyle Center
            CellStyle centerBorderStyle = workbook.createCellStyle();
            centerBorderStyle.setBorderRight(BorderStyle.THIN);
            centerBorderStyle.setBorderLeft(BorderStyle.THIN);
            centerBorderStyle.setBorderTop(BorderStyle.THIN);
            centerBorderStyle.setBorderBottom(BorderStyle.THIN);
            centerBorderStyle.setAlignment(HorizontalAlignment.CENTER);
            centerBorderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // Set Data CellStyle Left
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);

            // 2.1 System Name
            SXSSFCell systemNameCell = systemNameRow.createCell(2);
            systemNameCell.setCellValue(systemName);
            systemNameCell.setCellStyle(centerBorderStyle);
            SXSSFCell systemNameMergedCell = systemNameRow.createCell(3);
            systemNameMergedCell.setCellStyle(centerBorderStyle);
            sheet.addMergedRegion(CellRangeAddress.valueOf("C3:D3")); // Merged Title Cells

            // 3.0 Sub Title
            SXSSFRow subTitleRow = sheet.createRow(3);
            List<String> subTitleNameList = Arrays.asList("No", "테이블영문명", "테이블한글명", "비고");
            int subTitleIndex = 0;
            for(String subTitleName : subTitleNameList) {
                SXSSFCell subTitleCell = subTitleRow.createCell(subTitleIndex++);
                subTitleCell.setCellStyle(subTitleCellStyle);
                subTitleCell.setCellValue(subTitleName);
            }

            int rowCnt = 4;
            // Datas
            for(TableCommentDTO tableCommentDTO : tableCommentList) {
                SXSSFRow dataRow = sheet.createRow(rowCnt++);
                SXSSFCell noCell = dataRow.createCell(0);
                noCell.setCellValue(rowCnt-4);
                noCell.setCellStyle(centerBorderStyle);

                SXSSFCell tableCell = dataRow.createCell(1);
                tableCell.setCellValue(tableCommentDTO.getTableName());
                tableCell.setCellStyle(borderStyle);

                SXSSFCell commentCell = dataRow.createCell(2);
                commentCell.setCellValue(tableCommentDTO.getTableComment());
                commentCell.setCellStyle(borderStyle);

                SXSSFCell blankCell = dataRow.createCell(3);
                blankCell.setCellStyle(borderStyle);
            }

            /* tmp File */
            File tmpFile = File.createTempFile("TMP~", ".xlsx");
            try (OutputStream fos = new FileOutputStream(tmpFile)) {
                workbook.write(fos);
            }
            InputStream res = new FileInputStream(tmpFile) {
                @Override
                public void close() throws IOException {
                    super.close();
                    if (tmpFile.delete()) {
                        log.info("tmpFile Delete Success.");
                    }
                }
            };
            /* File End */

            return ResponseEntity.status(HttpStatus.OK)
                    .contentLength(tmpFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", String.format("attachment;filename=\"%s.xlsx\"", downloadFilename))
                    .body(new InputStreamResource(res));

        } catch (IOException e) {
            log.warn(e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("IOException")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        } catch (Exception e) {
            log.error("Unexpected Error : " + e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("Server Error")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        }
    }
}

package com.note.dbdocs.web;

import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.service.DbdocsExcelService;
import com.note.dbdocs.service.DbdocsPostgresService;
import com.note.dbdocs.util.DbdocsCellStyleUtils;
import com.note.dbdocs.util.DbdocsWorkbook;
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
    @Resource(name="dbdocsExcelService")
    DbdocsExcelService dbdocsExcelService;

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
                DbdocsWorkbook workbook = new DbdocsWorkbook();
        ) {

            // Create Sheet
            SXSSFSheet sheet = workbook.createSheet("테이블목록");
            sheet.setRandomAccessWindowSize(100); // 메모리 행 100개로 제한, 초과시 Disk로 flush
            sheet.setColumnWidth(0, 32*70); // 32 * 1 (1 pixel)
            sheet.setColumnWidth(1, 32*430);
            sheet.setColumnWidth(2, 32*430);
            sheet.setColumnWidth(3, 32*150);

            // sheet 에 표 테이블 추가
            workbook.createTableListTable(sheet, tableCommentList);

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

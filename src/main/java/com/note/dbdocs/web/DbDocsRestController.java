package com.note.dbdocs.web;

import com.note.dbdocs.dto.ColumnCommentDTO;
import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.service.DbdocsPostgresService;
import com.note.dbdocs.util.DbdocsColumnWorkbook;
import com.note.dbdocs.util.DbdocsTableWorkbook;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import com.note.utils.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Tag(name="PostgreSQL 코멘트 조회", description="PostgreSQL 코멘트 조회 설명설명")
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
     * @return {@link com.note.dbdocs.dto.TableCommentDTO} List
     */
    @Operation(summary="테이블 목록 조회", description="테이블 목록 조회")
    @Parameter(name="schema_name", description="조회할 스키마 명. 기본 public. 필수")
    @Parameter(name="table_name", description="조회할 테이블 명. 없으면 전체")
    @PutMapping(value="/postgresTableComment")
    public ResponseEntity<?> getTableComment(
            @RequestParam(name="schema_name", defaultValue="public") String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName
    ) {

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .build();
        log.debug(dbdocsSrchInfo.toString());

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
     * @return 엑셀 다운로드
     */
    @Operation(summary="테이블 목록 다운로드", description="테이블 목록 엑셀 다운로드")
    @Parameter(name="schema_name", description="조회할 스키마 명. 기본 public. 필수")
    @Parameter(name="table_name", description="조회할 테이블 명. 없으면 전체")
    @Parameter(name="system_name", description="입력할 시스템 명. 없으면 제목 없음")
    @GetMapping(value="/download/tableListExcel")
    public ResponseEntity<?> getDownloadTableList(
            @RequestParam(name="schema_name", defaultValue="public") String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName,
            @RequestParam(name="system_name", defaultValue="", required = false) String systemName
    ) {

        // 다운로드 되었을 떄 받을 파일 명 (확장자 미 포함)
        String downloadFilename = "Table_List";

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .build();
        log.debug(dbdocsSrchInfo.toString());

        // 조회
        List<TableCommentDTO> tableCommentList = dbdocsPostgresService.selectTableCommentDTOList(dbdocsSrchInfo);

        try (
                DbdocsTableWorkbook workbook = new DbdocsTableWorkbook();
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

    /**
     * PostgreSQL 테이블의 컬럼과 컬럼 코멘트를 조회
     * @param schemaName 스키마명, 기본값 public
     * @param tableName 테이블명, 없으면 전체
     * @param columnName 컬럼명, 없으면 전체
     * @return {@link com.note.dbdocs.dto.ColumnCommentDTO} List
     */
    @Operation(summary="테이블 컬럼목록 조회", description="테이블 명세서 엑셀 다운로드를 위한 컬럼 목록 조회")
    @Parameter(name="schema_name", description="조회할 스키마 명. 기본 public. 필수")
    @Parameter(name="table_name", description="조회할 테이블 명. 없으면 전체")
    @Parameter(name="column_name", description="조회할 컬럼 명. 없으면 전체")
    @PostMapping("/postgresColumnComment")
    public ResponseEntity<?> getColumnComment(
            @RequestParam(name="schema_name", defaultValue="public") String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName,
            @RequestParam(name="column_name", defaultValue="", required = false) String columnName
    ) {

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .columnName(columnName)
                .build();
        log.debug(dbdocsSrchInfo.toString());

        // 조회
        List<ColumnCommentDTO> columnCommentList = dbdocsPostgresService.selectTableColumnCommentDTOList(dbdocsSrchInfo);

        // 조회 결과가 없는 경우
        if (columnCommentList.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(columnCommentList);
    }

    /**
     * PostgreSQL 테이블 코멘트 조회
     * @param schemaName    조회 할 스키마 명
     * @param tableName     테이블 명, 없으면 전체
     * @param columnName    컬럼 명, 없으면 전체
     * @param systemName    시스템 명
     * @return
     */
    @Operation(summary="테이블 명세서 다운로드", description="테이블 명세서 엑셀 다운로드")
    @Parameter(name="schema_name", description="조회할 스키마 명. 기본 public. 필수")
    @Parameter(name="table_name", description="조회할 테이블 명. 없으면 전체")
    @Parameter(name="column_name", description="조회할 컬럼 명. 없으면 전체")
    @Parameter(name="system_name", description="입력할 시스템 명. 없으면 공백")
    @Parameter(name="write_date", description="입력할 날짜. 없으면 오늘")
    @Parameter(name="writer", description="입력할 작성자 명. 없으면 공백")
    @GetMapping(value="/download/columnListExcel")
    public ResponseEntity<?> getDownloadColumnList(
            @RequestParam(name="schema_name", defaultValue="public") String schemaName,
            @RequestParam(name="table_name", defaultValue="", required = false) String tableName,
            @RequestParam(name="column_name", defaultValue="", required = false) String columnName,
            @RequestParam(name="system_name", defaultValue="", required = false) String systemName,
            @RequestParam(name="write_date", defaultValue="", required = false) String writeDate,
            @RequestParam(name="writer", defaultValue="", required = false) String writer
    ) {
        // 초기값
        if (writeDate.equals("")) {
            Date now = new Date();
            writeDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
            log.info("writeDate: {}", writeDate);
        }
        
        // 다운로드 되었을 떄 받을 파일 명 (확장자 미 포함)
        String downloadFilename = "Table_Definition";

        // 검색 조건
        DbdocsSrchInfo dbdocsSrchInfo = DbdocsSrchInfo.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .columnName(columnName)
                .build();
        log.debug(dbdocsSrchInfo.toString());

        // 조회
        List<ColumnCommentDTO> tableCommentList = dbdocsPostgresService.selectTableColumnCommentDTOList(dbdocsSrchInfo);

        try (
                DbdocsColumnWorkbook workbook = new DbdocsColumnWorkbook();
        ) {

            // Create Sheet
            SXSSFSheet sheet = workbook.createSheet("테이블정의서");
            sheet.setRandomAccessWindowSize(100); // 메모리 행 100개로 제한, 초과시 Disk로 flush
            sheet.setColumnWidth(0, 32*28); // 32 * 1 (1 pixel)
            sheet.setColumnWidth(1, 32*187);
            sheet.setColumnWidth(2, 32*277);
            sheet.setColumnWidth(3, 32*115);
            sheet.setColumnWidth(4, 32*70);
            sheet.setColumnWidth(5, 32*70);
            sheet.setColumnWidth(6, 32*70);
            sheet.setColumnWidth(7, 32*70);
            sheet.setColumnWidth(8, 32*97);
            sheet.setColumnWidth(9, 32*97);

            // sheet 에 표 테이블 추가
            workbook.createTableSheet(sheet, tableCommentList, systemName, writeDate, writer);

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

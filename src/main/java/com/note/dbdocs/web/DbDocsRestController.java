package com.note.dbdocs.web;

import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.service.DbdocsPostgresService;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.note.dbdocs.dto;

import lombok.Data;

/**
 * 테이블 명과 테이블 코멘드를 담은 DTO
 */
@Data
public class TableCommentDTO {

    private String tableName; // 테이블 명
    private String tableComment; // 테이블 코멘트
}

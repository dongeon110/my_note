package com.note.dbdocs.dto;

import lombok.Data;

@Data
public class ColumnCommentDTO {

    private String schemaName = "public"; // 스키마명
    private String tableName; // 테이블명
    private String tableComment; // 테이블 코멘트
    private String columnName; // 컬럼명
    private String columnComment; // 컬럼 코멘트
    private String columnType; // 컬럼 타입
    private Integer columnLength; // 컬럼 길이
    private String columnIsNullable; // 컬럼 null 여부 Y: nullable / N: not null
    private String pk; // Primary Key 여부 PK: pk / null: pk아님
    private String defaultValue; // Default 기본 값

}

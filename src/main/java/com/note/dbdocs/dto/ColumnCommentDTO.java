package com.note.dbdocs.dto;

import lombok.Data;

@Data
public class ColumnCommentDTO {

    String schemaName = "public"; // 스키마명
    String tableName; // 테이블명
    String tableComment; // 테이블 코멘트
    String columnName; // 컬럼명
    String columnComment; // 컬럼 코멘트
    String columnType; // 컬럼 타입
    Integer columnLength; // 컬럼 길이
    String columnIsNullable; // 컬럼 null 여부 Y: nullable / N: not null
    String pk; // Primary Key 여부 PK: pk / null: pk아님

}

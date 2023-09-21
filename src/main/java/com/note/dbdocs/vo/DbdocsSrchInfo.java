package com.note.dbdocs.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbdocsSrchInfo {

    private String schemaName = "public"; // 스키마 명 default "public"
    private String tableName; // 테이블 명
    private String columnName; // 컬럼 명
}

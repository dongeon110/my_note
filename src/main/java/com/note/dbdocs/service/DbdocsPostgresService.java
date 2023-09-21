package com.note.dbdocs.service;

import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.vo.DbdocsSrchInfo;

import java.util.List;

public interface DbdocsPostgresService {

    /**
     * PostgreSQL DB의 테이블이름과 테이블코멘트를 조회
     * @param dbdocsSrchInfo 검색조건, tableName이 포함된 테이블 이름을 조회
     * @return 테이블 명이 검색조건의 tableName을 포함하고 있는 테이블 조회, tableName이 없는 경우, 전체 조회
     * @since 23.09.20
     */
    public List<TableCommentDTO> selectTableCommentDTOList(DbdocsSrchInfo dbdocsSrchInfo);
}

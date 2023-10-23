package com.note.dbdocs.mapper;

import com.note.dbdocs.dto.ColumnCommentDTO;
import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DbdocsPostgresMapper {

    /**
     * PostgreSQL DB의 테이블이름과 테이블코멘트를 조회
     * @param dbdocsSrchInfo 검색조건, tableName이 포함된 테이블 이름을 조회
     * @return 테이블 명이 검색조건의 tableName을 포함하고 있는 테이블 조회, tableName이 없는 경우, 전체 조회
     * @since 23.09.20
     */
    List<TableCommentDTO> selectTableCommentDTOList(@Param("dbdocsSrchInfo") DbdocsSrchInfo dbdocsSrchInfo);

    /**
     * PostgreSQL DB의 테이블이름 / 테이블 코멘트 / 컬럼명 / 컬럼코멘트 조회
     * @param dbdocsSrchInfo 검색조건, 스키마명 default: public
     * @return table명 없는 경우 전체, 컬럼명 없는 경우 전체
     */
    List<ColumnCommentDTO> selectTableColumnCommentDTOList(@Param("dbdocsSrchInfo") DbdocsSrchInfo dbdocsSrchInfo);
}

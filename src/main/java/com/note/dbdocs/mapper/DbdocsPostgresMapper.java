package com.note.dbdocs.mapper;

import com.note.dbdocs.dto.TableCommentDTO;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DbdocsPostgresMapper {

    /**
     * PostgreSQL DB의 테이블이름과 테이블코멘트를 조회
     * @param dbdocsSrchInfo 검색조건, tableName이 포함된 테이블 이름을 조회
     * @return 테이블 명이 검색조건의 tableName을 포함하고 있는 테이블 조회, tableName이 없는 경우, 전체 조회
     * @since 23.09.20
     */
    @Select("SELECT pgt.tablename AS table_name, sub.table_comment\n" +
            "        FROM pg_catalog.pg_tables AS pgt\n" +
            "        LEFT JOIN (SELECT b.relname     AS table_name\n" +
            "                        , a.description AS table_comment\n" +
            "                   FROM   pg_catalog.pg_description a\n" +
            "                   FULL JOIN pg_catalog.pg_stat_all_tables b\n" +
            "                   ON        a.objoid = b.relid\n" +
            "                   WHERE  objoid IN (SELECT oid\n" +
            "                                    FROM   pg_class\n" +
            "                                    WHERE  relnamespace = (SELECT oid\n" +
            "                                                           FROM   pg_catalog.pg_namespace\n" +
            "                                                           WHERE  nspname = #{dbdocsSrchInfo.schemaName})\n" +
            "                                    )\n" +
            "                   AND    objsubid = 0) AS sub\n" +
            "        ON pgt.tablename = sub.table_name\n" +
            "        WHERE schemaname = #{dbdocsSrchInfo.schemaName}\n" +
            "        AND pgt.tablename LIKE '%'||#{dbdocsSrchInfo.tableName}||'%'\n" +
            "        ORDER BY pgt.tablename;")
    List<TableCommentDTO> selectTableCommentDTOList(@Param("dbdocsSrchInfo") DbdocsSrchInfo dbdocsSrchInfo);
}

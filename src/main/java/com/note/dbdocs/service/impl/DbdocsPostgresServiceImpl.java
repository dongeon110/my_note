package com.note.dbdocs.service.impl;

import com.note.dbdocs.dto.ColumnCommentDTO;
import com.note.dbdocs.dto.TableCommentDTO;

import com.note.dbdocs.mapper.DbdocsPostgresMapper;
import com.note.dbdocs.service.DbdocsPostgresService;
import com.note.dbdocs.vo.DbdocsSrchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dbdocsPostgresService")
public class DbdocsPostgresServiceImpl implements DbdocsPostgresService {

    private final DbdocsPostgresMapper dbdocsPostgresMapper;
    public DbdocsPostgresServiceImpl(DbdocsPostgresMapper dbdocsPostgresMapper) {
        this.dbdocsPostgresMapper = dbdocsPostgresMapper;
    }

    @Override
    public List<TableCommentDTO> selectTableCommentDTOList(DbdocsSrchInfo dbdocsSrchInfo) {
        return dbdocsPostgresMapper.selectTableCommentDTOList(dbdocsSrchInfo);
    }

    @Override
    public List<ColumnCommentDTO> selectTableColumnCommentDTOList(DbdocsSrchInfo dbdocsSrchInfo) {
        return dbdocsPostgresMapper.selectTableColumnCommentDTOList(dbdocsSrchInfo);
    }
}

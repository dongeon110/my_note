package com.note.board.repository.custom.impl;

import com.note.board.domain.Board;
import com.note.board.repository.custom.CustomBoardRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements CustomBoardRepository {

    public BoardRepositoryImpl() {
        super(Board.class);
    }
}

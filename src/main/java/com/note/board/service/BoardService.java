package com.note.board.service;

import com.note.board.domain.Board;
import com.note.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * findAll
     * @return
     */
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    /**
     * save
     * @param board
     * @return
     */
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    /**
     * delete
     */
    public void delete(Board board) {
        boardRepository.delete(board);
    }

}

package com.note.board.service;

import com.note.board.domain.Board;
import com.note.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * findById
     * @param id
     * @return
     */
    public Optional<Board> getBoard(Long id) {
        return boardRepository.findById(id);
    }

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

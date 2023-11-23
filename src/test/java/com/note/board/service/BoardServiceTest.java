package com.note.board.service;

import com.note.board.domain.Board;
import com.note.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("전체 조회시 결과 있음")
    void getBoardList_When_전체조회_Expect_결과있음() {
        //given
        //when
        List<Board> boardList = boardService.getBoardList();
        //then
        assertThat(boardList)
                .isNotEmpty();

    }

    @Test
    @DisplayName("기본 시퀀스 값으로 Board 저장")
    @Transactional
    void saveBoard() {
        //given
        Board board = new Board();
        board.setTitle("테스트 제목");
        board.setWriter(1L); // Test
        //when
        Board resultBoard = boardService.saveBoard(board);
        //then
        //id
        assertThat(resultBoard.getBoardIndex())
                .isGreaterThan(1L);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
    }
}
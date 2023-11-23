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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    private Long testBoardIndex = 3L;

    @Test
    @DisplayName("전체 조회시 결과 있음")
    void getBoardList_When_전체조회_Expect_결과있음() {
        //given
        //when
        List<Board> boardList = boardService
                .getBoardList();
        //then
        assertThat(boardList)
                .isNotEmpty();
    }

    @Test
    @DisplayName("테스트 Board 데이터 1개 조회")
    void getBoard_Expect_테스트_제목() {
        //given
        Board expectedBoard = new Board();
        expectedBoard.setBoardIndex(testBoardIndex);
        expectedBoard.setTitle("테스트 제목");
        expectedBoard.setWriter(1L);

        //when
        Board actualBoard = boardService
                .getBoard(testBoardIndex)
                .get();

        //then
        assertThat(actualBoard)
                .isEqualTo(expectedBoard);
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
        Board resultBoard = boardService
                .saveBoard(board);
        //then
        //id
        assertThat(resultBoard.getBoardIndex())
                .isGreaterThan(1L);
    }

    @Test
    @DisplayName("삭제 테스트")
    @Transactional
    void delete() {
        //given
        Board board = new Board();
        board.setBoardIndex(testBoardIndex); // 삭제할 데이터
        
        getBoard_Expect_테스트_제목(); // 기존 테스트 데이터는 있어야 함

        //when
        boardService.delete(board);

        //then
        Optional<Board> maybeBoard = boardService.getBoard(testBoardIndex);

        assertThat(maybeBoard)
                .isEmpty();
    }
}
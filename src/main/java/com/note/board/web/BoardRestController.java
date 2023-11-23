package com.note.board.web;

import com.note.board.domain.Board;
import com.note.board.service.BoardService;
import com.note.utils.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="JPA Test API", description="JPA Test API")
@Slf4j
@RestController
@RequestMapping("/board/api")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @Operation(summary="Read", description="전체 조회 테스트")
    @GetMapping("/read.do")
    public ResponseEntity read() {
        List<Board> boardList = boardService.getBoardList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(boardList);
    }

    @Operation(summary="Create", description="추가 테스트")
    @PutMapping("/create.do")
    public ResponseEntity create() {

        Board board = new Board();
        board.setTitle("test Title");
        board.setWriter(2L);

        Board result = boardService.saveBoard(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary="Update", description="추가 테스트")
    @PostMapping("/update.do")
    public ResponseEntity update() {

        Board board = new Board();
        board.setBoardIndex(3L);
        board.setTitle("title update Test");
        board.setWriter(2L);

        Board result = boardService.saveBoard(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary="Delete", description="추가 테스트")
    @DeleteMapping("/delete.do")
    public ResponseEntity delete() {

        Board board = new Board();
        board.setBoardIndex(3L);

        boardService.delete(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @Operation(summary="테스트2", description="테스트2")
    @GetMapping("/asdf.do")
    public ResponseEntity asdf(
            @RequestParam(value="hi", required = false, defaultValue = "") String hi,
            @RequestParam(value="hello") String hello
    ) {
        log.info("hi: {}", hi);
        log.info("hello: {}", hello);
        if(hi.equals("no")){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("Failed.")
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseDTO);
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .message("Success.")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDTO);
    }
}

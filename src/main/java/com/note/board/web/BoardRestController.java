package com.note.board.web;

import com.note.board.domain.Board;
import com.note.board.service.BoardService;
import com.note.utils.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name="Board", description="JPA Board API")
@Slf4j
@RestController
@RequestMapping("/board/api")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @Operation(summary="전체 조회", description="전체 조회 테스트")
    @GetMapping("/readAll")
    public ResponseEntity readAll() {
        List<Board> boardList = boardService.getBoardList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(boardList);
    }
    
    @Operation(summary="1개 조회", description="1개만 조회")
    @GetMapping("/read/{id}")
    public ResponseEntity read(
            @PathVariable Long id
    ) {
        Optional<Board> maybeBoard = boardService.getBoard(id);
        if(maybeBoard.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(maybeBoard.get());
    }

    @Operation(summary="Create", description="생성 테스트")
    @PutMapping("/create")
    public ResponseEntity create(
            @RequestParam String title,
            @RequestParam Long writer
    ) {
        Board board = Board.builder()
                .title(title)
                .writer(writer)
                .build();

        Board result = boardService.saveBoard(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary="Update", description="수정 테스트")
    @PostMapping("/update/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestParam String title
    ) {
        Board board = Board.builder()
                .boardIndex(id)
                .title(title)
                .build();
        Board result = boardService.saveBoard(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary="Delete", description="삭제 테스트")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(
            @PathVariable Long id
    ) {
        Board board = Board.builder()
                .boardIndex(id)
                .build();

        boardService.delete(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}

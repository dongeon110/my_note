package com.note.board.web;

import com.note.utils.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="JPA Test API", description="JPA Test API")
@Slf4j
@RestController
@RequestMapping("/board/api")
public class BoardRestController {

    @Operation(summary="테스트1", description="테스트1")
    @GetMapping("/list.do")
    public ResponseEntity list() {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .message("This is Message.")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDTO);
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

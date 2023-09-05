package com.note.board.web;

import com.note.utils.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/board/api")
public class BoardRestController {

    @GetMapping("/list.do")
    public ResponseEntity list() {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .message("This is Message.")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDTO);
    }
}

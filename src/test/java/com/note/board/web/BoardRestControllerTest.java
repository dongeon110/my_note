package com.note.board.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

@WebMvcTest(controllers = BoardController.class)
public class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean // Service단

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    
    @DisplayName("GET /board/api/list.do Test")
    @Test
    void testList() throws Exception {
        //given
    }


}

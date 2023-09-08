package com.note.board.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="board")
@Data
public class Board {
    @Id
    @Column(name="board_index")
    private Long boardIndex;

    @Column(name="title")
    private String title;

    @Column(name="writer")
    private Long writer;
}

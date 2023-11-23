package com.note.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="board")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // postgres seq
    @Column(name="board_index")
    private Long boardIndex;

    @Column(name="title")
    private String title;

    @Column(name="writer")
    private Long writer;
}

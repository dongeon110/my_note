package com.note.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="myusers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyUser {

    @Id
    @Column(name="user_idx")
    private Long userIndex;

    @Column(name="user_id")
    private String userId;

    @Column(name="user_pw")
    private String userPassword;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_email")
    private String userEmail;
}

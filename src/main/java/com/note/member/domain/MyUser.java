package com.note.member.domain;

import jakarta.persistence.*;
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

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private Role role;
}

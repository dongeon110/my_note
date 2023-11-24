package com.note.member.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {

    ROLE_USER("사용자"),
    ROLE_ADMIN("관리자"),
    ROLE_ANONYMOUS("접속자");

    String kor;
}

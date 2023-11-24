package com.note.member.repository;

import com.note.member.domain.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUserId(String userId);
}

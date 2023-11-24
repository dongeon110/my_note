package com.note.member.service;

import com.note.member.domain.MyUser;
import com.note.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<MyUser> findOne(String userId) {
        return memberRepository.findByUserId(userId);
    }
}

package com.note.member.service;

import com.note.member.domain.MyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MyUser user = memberService.findOne(userId)
                .orElseThrow(() -> new UsernameNotFoundException("그런 유저 없음"));

        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPassword())
                .roles(user.getRole().name())
                .build();
    }
}

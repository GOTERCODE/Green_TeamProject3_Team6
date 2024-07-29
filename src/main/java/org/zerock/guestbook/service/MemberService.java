package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }
}

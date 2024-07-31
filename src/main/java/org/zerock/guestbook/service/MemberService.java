package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.MemberRepository;
import org.zerock.guestbook.repository.MemberRepository_Register;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRepository_Register memberRepository_register;



    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    public Member Register_Test(String userEmail, String userNick, String register_username, String userpassword) {

        checkForDuplicates(userEmail, userNick, register_username);

        // 새로운 Member 객체 생성
        Member newMember = new Member();
        newMember.setEmail(userEmail);
        newMember.setNickname(userNick);
        newMember.setUsername(register_username);
        newMember.setPassword(userpassword); // 비밀번호 암호화

        // 데이터베이스에 저장
        return memberRepository.save(newMember);
    }

    private void checkForDuplicates(String email, String nickname, String username) {
        if (memberRepository_register.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("해당 이메일을 사용 중인 유저가 있습니다.");
        }
        if (memberRepository_register.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("해당 닉네임을 사용 중인 유저가 있습니다.");
        }
        if (memberRepository_register.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("해당 ID를 사용 중인 유저가 있습니다.");
        }
    }

}

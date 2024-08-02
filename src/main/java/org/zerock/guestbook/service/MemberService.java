package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.MemberRepository;
import org.zerock.guestbook.repository.MemberRepository_Register;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRepository_Register memberRepository_register;



    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);

        if(member != null){
            try {
                // 해시를 생성할 문자열
                String string1 = password;

                // SHA-256 해시 객체 생성
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                // 문자열을 바이트 배열로 변환하고 해시 계산
                byte[] hashBytes = digest.digest(string1.getBytes());

                // 바이트 배열을 16진수 문자열로 변환
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    // 바이트를 2자리 16진수로 변환하여 StringBuilder에 추가
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }

                // 해시된 비밀번호와 데이터베이스의 비밀번호 비교
                if (member.getPassword().equals(hexString.toString())) {
                    return member; // 로그인 성공
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            return null;
        }

    }

    public Member Register_Test(String userEmail, String userNick, String register_username, String userpassword) {

        checkForDuplicates(userEmail, userNick, register_username);

        // 새로운 Member 객체 생성
        Member newMember = new Member();
        newMember.setEmail(userEmail);
        newMember.setNickname(userNick);
        newMember.setUsername(register_username);
        //newMember.setPassword(userpassword);

        try {
            // 해시를 생성할 문자열
            String string1 = userpassword;

            // SHA-256 해시 객체 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 문자열을 바이트 배열로 변환하고 해시 계산
            byte[] hashBytes = digest.digest(string1.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // 바이트를 2자리 16진수로 변환하여 StringBuilder에 추가
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            newMember.setPassword(hexString.toString());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }




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


//    public Member findById(String id) {
//        return memberRepository.findById(id).orElse(null);
//    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }


}

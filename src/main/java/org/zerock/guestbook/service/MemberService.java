package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.BoardFreeRepository;
import org.zerock.guestbook.repository.BoardFreeRepository2;
import org.zerock.guestbook.repository.MemberRepository;
import org.zerock.guestbook.repository.MemberRepository_Register;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardFreeRepository2 boardFreeRepository2;

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

    // 회원정보 업데이트 메소드
    public Member updateMember(String email, String nickname, String username, String password) {
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        member.setEmail(email);
        member.setNickname(nickname);
        member.setUsername(username);


        if (!password.isEmpty()) {
            try {
                String hashedPassword = hashPassword(password);
                member.setPassword(hashedPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("비밀번호 해시 처리 중 오류 발생.");
            }
        }

        // 업데이트된 정보를 데이터베이스에 저장
        return memberRepository.save(member);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 회원정보 업데이트 메소드
    public Member updateMember2(String email, String nickname, String username, String password) {
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        member.setEmail(email);
        member.setNickname(nickname);
        member.setUsername(username);
        member.setPassword(password);

        return memberRepository.save(member);
    }


    public void updateBoard(String oldNick, String newNick) {
        boardFreeRepository2.updateWriter(oldNick, newNick);
    }

    public Member like_serch(String username) {
        return memberRepository.findByUsername2(username);
    }

}
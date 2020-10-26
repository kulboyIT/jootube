package me.weekbelt.wetube.modules.member;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFactory {

    @Autowired
    MemberRepository memberRepository;

    public Member createMember(String name) {
        Member member = Member.builder()
                .name(name)
                .email(name + "@email.com")
                .password("12345678")
                .role(Role.USER)
                .build();
        return memberRepository.save(member);
    }
}

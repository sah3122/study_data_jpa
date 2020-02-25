package me.study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import me.study.datajpa.dto.MemberDto;
import me.study.datajpa.entity.Member;
import me.study.datajpa.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(MemberDto::new);
        PageRequest request = PageRequest.of(1, 2);

        Page<MemberDto> map = memberRepository.findAll(request)
                .map(MemberDto::new);
        return map;
    }

    @PostConstruct
    public void init() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user", i));
        }
    }
}

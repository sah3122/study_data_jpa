package me.study.datajpa.repository;

import me.study.datajpa.entity.Member;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamName) {
        return (Specification<Member>) (root, query, builder) -> {
            return null;
        };
    }
}

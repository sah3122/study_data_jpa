package me.study.datajpa.entity;

import me.study.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("=> member.team = " + member.getTeam());
        }

        member1.changeTeam(teamB);
        em.merge(member1);

        em.flush();
        em.clear();

        Member singleResult = em.createQuery("select m from Member m where m.username = 'member1'", Member.class).getSingleResult();
        List<Team> teams = em.createQuery("select t from Team t", Team.class).getResultList();
        for (Team team : teams) {
            System.out.println(team.getMembers());
        }
        System.out.println(singleResult.getTeam());
    }

    @Test
    void jpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1");
        memberRepository.save(member);  //@Prepersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println(findMember.getCreatedDate());
        System.out.println(findMember.getLastModifiedDate());
        System.out.println(findMember.getCreatedBy());
        System.out.println(findMember.getLastModifiedBy());
    }
}
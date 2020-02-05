package me.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    @Column(name ="team_id")
    private Long id;
    private String name;

    public Team(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "team") // mappedBy 는 FK가 없는 쪽에 거는것이 좋다.
    private List<Member> members = new ArrayList<>();
}

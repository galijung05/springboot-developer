package me.blogSpringBoot.springbootdeveloper.test;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@AllArgsConstructor
@Getter
@Setter
@Entity // 엔티티 지정
public class Member {

    @Id // 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1 증가
    @Column(name = "id", updatable = false)
    private Long id; // DB 테이블의 'id' 컬럼과 매칭

    @Column(name = "name", nullable = false)
    private String name; // DB 테이블의 'name' 컬럼과 매칭
}

package com.movie.boot4.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
//연관 관계의 엔티티 사용하지 않도록 설정
@ToString(exclude = {"movie", "member"})
public class Review extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    //다대일 관계에서는 항상 기본값이 즉시 로딩이므로, 지연 로딩으로 설정하는 작업 필요
    //-> 지연로딩을 사용하므로, 테스트시에 @Transactional 필요
    @ManyToOne (fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    private int grade;

    private String text;

    //리뷰 수정을 위한 메서드 작성
    public void changeGrade(int grade){
        this.grade=grade;
    }
    public void changeText(String text){
        this.text=text;
    }
}

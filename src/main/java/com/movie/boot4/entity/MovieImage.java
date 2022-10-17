package com.movie.boot4.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "movie")
//연관 관계가 존재할 경우에, ToString을 사용할 때에 반복 주의
public class MovieImage {
    //값 객체가 아닌 엔티티객체로 설정하는 이유는 페이지 처리나 조인 처리가 많으므로
    //단방향 참조로 처리해 @Query를 통해 조인을 사용하므로, JPQL에서 엔티티일 경우 사용이 자유롭다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    //uuid클래스를 이용해 고유한 번호를 생성해 사용
    private String uuid;

    private String imgName;

    //이미지 저장경로는 년/월/일 구조를 의미한다.
    private String path;

    //movie 테이블이 PK를 가지며, movieimage테이블이 FK를 가지는 구조
    //다대일의 경우 지연로딩 설정 필요
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}

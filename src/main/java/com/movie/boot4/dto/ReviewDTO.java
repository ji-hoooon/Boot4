package com.movie.boot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    //리뷰 번호
    private Long reviewnum;

    //영화 번호
    private Long mno;

    //회원 번호
    private Long mid;

    //회원 정보
    private Long id;
    private String nickname;
    private String email;

    //리뷰 정보
    private int grade;
    private String text;
    private LocalDateTime regDate, modDate;

}

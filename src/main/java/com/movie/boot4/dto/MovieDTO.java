package com.movie.boot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long mno;
    private String title;

    //특정 필드를 특정 값으로 초기화하기위한 어노테이션
    @Builder.Default
    private List<MovieImageDTO> imageDTOList=new ArrayList<>();
    //영화 이미지도 같이 수집해 전달하므로, 내부에 리스트를 이용해 수집한다.
}

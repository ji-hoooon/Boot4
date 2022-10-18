package com.movie.boot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


//화면에 전달되는 게시물 목록 처리
@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    private int page;
    private int size;

    //화면에 전달되는 목록을 위한 생성자 : 1페이지부터 시작하고, 10페이지 단위로 목록 처리를 의미
    public PageRequestDTO(){
        this.page=1;
        this.size=10;
    }
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
        //정적으로 생성하기 위해 of를 사용하고, JPA에서 페이지는 0부터 시작하므로 page-1로 전달;
    }
}

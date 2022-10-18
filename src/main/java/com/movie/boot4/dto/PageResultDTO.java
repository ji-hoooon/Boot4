package com.movie.boot4.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//화면에 필요한 결과 게시물 목록
@Data

//DTO -> Entity로 변환하기 때문에
public class PageResultDTO<DTO, EN> {

    //DTO리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //페이지 번호  목록
    private List<Integer> pageList;


    //function 패키지에 있는 람다식으로 사용할 수 있는 편리한 함수형 메서드 :
    //매개변수와 반환값 존재 여부에 따라 구분 / 조건식 표현해 참 거짓 반환
    // Runnable -> run(), Supplier -> get(), Consumer -> accept(), Function -> apply() , Predicate -> test()

    //Page<Entity>의 객체들을 DTO객체로 변환해서 담는 메서드
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        //페이징 결과를 스트림에 담아서, fn을 이용해 람다식을 통해 변환하고, 최종연산으로 리스트로 반환
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }//Function 함수형 인터페이스를 사용하면, 제네릭으로 정의되어 있기 때문에 어떤 엔티티를 전달해도 재사용 가능


    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1; // JPA가 전달한 페이지는 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //임시 끝 번호
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        //끝 번호 처리 : 임시 끝 번호와 크기 비교해 실제 마지막 번호와 임시 끝번호 중 선택
        end = totalPage > tempEnd ? tempEnd: totalPage;

        next = totalPage > tempEnd;


        //페이지 리스트
        //기본형 int스트림을 스트림으로 변환하는 메서드 : mapToObj()와 boxed()
        //int스트림을 이용해 범위 제한해 데이터를 추출하고, 다시 Stream<Integer>로 변환 후, 최종연산으로 담아서 리스트로 변환
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        //pageList는 List<Integer>
    }

}

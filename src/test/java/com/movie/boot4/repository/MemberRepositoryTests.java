package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member= Member.builder()
                    .email("r"+i+"@naver.com")
                    .pw("1234")
                    .nickname("reviewer"+i)
                    .build();
            memberRepository.save(member);
        });
    }

    //회원 삭제시 리뷰 삭제후 회원 삭제하는 메서드 테스트

    //리뷰가 존재할 때 회원 삭제 오류 발생
    //(1) 참조 무결성 제약조건 위배
    //-> FK를 가지는 Review 쪽을 먼저 삭제하지 않았기 때문에
    //(2) 트랜잭션 관련 처리 미비
    //-> 회원 삭제시 회원이 작성한 리뷰와 회원 삭제는 동시에 일어나야하는 조건
    @Transactional
    @Commit
    @Test
    public void testDeleteMember(){
        //삭제할 멤버의 번호와 해당 멤버객체 생성
        Long mid = 1L;
        Member member = Member.builder()
                .mid(mid)
                .build();

        //FK위배의 경우
        //memberRepository.deleteById(mid);
        //reviewRepository.deleteByMember(member);

        //@Transactional과 @Commit 추가 후에도
        // 비효율적인 SQL -> 하나씩 삭제하기 위해 리뷰 개수만큼 반복 실행
        //: @Query를 이용해 where절 지정
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }
}

package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

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
}

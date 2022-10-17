package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReviews(){

        IntStream.rangeClosed(1,200).forEach(i->{

            //��ȭ ��ȣ�� �ش��ϴ� ��ȭ�� ����� ��ȣ�� �ش��ϴ� ���� ���並 �ۼ��ؾ��ϹǷ�
            //��ȭ��ȣ, ������ȣ, �����ȣ
            //-> ��� ��ü, ��ȭ ��ü, ���� ��ü ������ ����
            Long mno=(long)(Math.random() *100)+1;

            Long mid=((long)(Math.random()*100)+1);


            //��� ��ü
            Member member =Member.builder()
                    .mid(mid)
                    .build();

            //���� ��ü
            Review movieReview=Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random()*5)+1)
                    .text("�� ��ȭ�� ���� ����..."+i)
                    .build();

            reviewRepository.save(movieReview);

        });

    }
}

package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReviews() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            //영화 번호에 해당하는 영화에 리뷰어 번호에 해당하는 리뷰어가 리뷰를 작성해야하므로
            //영화번호, 리뷰어번호, 리뷰번호
            //-> 멤버 객체, 영화 객체, 리뷰 객체 생성해 저장
            Long mno = (long) (Math.random() * 100) + 1;

            Long mid = ((long) (Math.random() * 100) + 1);


            //멤버 객체
            Member member = Member.builder()
                    .mid(mid)
                    .build();

            //리뷰 객체
            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌..." + i)
                    .build();

            reviewRepository.save(movieReview);

        });
    }

        //특정 영화의 모든 리뷰와 회원의 닉네임 조회 테스트
        @Transactional
        @Test
        public void testGetMovieReviews(){
            //필요한 정보
            //(1) 영화 객체
            //(2) 찾은 영화의 리뷰 정보를 담은 리뷰 리스트

            Movie movie=Movie.builder().mno(92L).build();

            List<Review> result = reviewRepository.findByMovie(movie);

            //reuslt를 돌면서 리뷰 정보 출력하는 forEach문
            result.forEach(movieReview -> {
                System.out.print(movieReview.getReviewnum());
                System.out.print("\t"+movieReview.getGrade());
                System.out.print("\t"+movieReview.getText());
                System.out.print("\t"+movieReview.getMember());
                System.out.print("\t"+movieReview.getMember().getEmail());
                System.out.println("---------------------------");

            });
        }

}

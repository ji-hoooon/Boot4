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

            //��ȭ ��ȣ�� �ش��ϴ� ��ȭ�� ����� ��ȣ�� �ش��ϴ� ���� ���並 �ۼ��ؾ��ϹǷ�
            //��ȭ��ȣ, ������ȣ, �����ȣ
            //-> ��� ��ü, ��ȭ ��ü, ���� ��ü ������ ����
            Long mno = (long) (Math.random() * 100) + 1;

            Long mid = ((long) (Math.random() * 100) + 1);


            //��� ��ü
            Member member = Member.builder()
                    .mid(mid)
                    .build();

            //���� ��ü
            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("�� ��ȭ�� ���� ����..." + i)
                    .build();

            reviewRepository.save(movieReview);

        });
    }

        //Ư�� ��ȭ�� ��� ����� ȸ���� �г��� ��ȸ �׽�Ʈ
        @Transactional
        @Test
        public void testGetMovieReviews(){
            //�ʿ��� ����
            //(1) ��ȭ ��ü
            //(2) ã�� ��ȭ�� ���� ������ ���� ���� ����Ʈ

            Movie movie=Movie.builder().mno(92L).build();

            List<Review> result = reviewRepository.findByMovie(movie);

            //reuslt�� ���鼭 ���� ���� ����ϴ� forEach��
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

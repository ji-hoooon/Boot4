package com.movie.boot4.service;

import com.movie.boot4.dto.ReviewDTO;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import com.movie.boot4.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    //interface와 달리 다른 곳에서 호출을 할 메서드이므로 public으로 선언
    //(1) 엔티티를 DTO로 변환
    //(2) DTO를 엔티티로 변환

    private final ReviewRepository reviewRepository;

    //특정 영화의 모든 리뷰를 가저온다. -> 엔티티로 리포지토리에서 원하는 리뷰를 찾아 DTO로 변환해 리스트로 반환
    @Override
    public List<ReviewDTO> getListOfMovie(Long mno){
        Movie movie=Movie.builder()
                .mno(mno)
                .build();
        List<Review> result=reviewRepository.findByMovie(movie);

        return result.stream()
                .map(movieReview -> entityToDTO(movieReview))
                .collect(Collectors.toList());
    }

    @Override
    //영화 리뷰를 추가 -> DTO를 엔티티로 변환해 리포지토리에 저장 후, DTO의 리뷰번호 반환
    public Long register(ReviewDTO movieReviewDTO){
        Review review=dtoToEntity(movieReviewDTO);

        reviewRepository.save(review);

        return movieReviewDTO.getReviewnum();
    }

    @Override
    //특정한 영화리뷰 수정 -> null일 수 있으므로 Optional 사용해, DTO로 리뷰를 찾고, 있으면 엔티티에서 수정 후, 리포지토리에 저장
    public void modify(ReviewDTO movieReviewDTO){
        Optional<Review> result=reviewRepository.findById(movieReviewDTO.getReviewnum());

        if(result.isPresent()){
            Review review = result.get();
            review.changeGrade(movieReviewDTO.getGrade());
            review.changeText(movieReviewDTO.getText());

            reviewRepository.save(review);
        }
    }

    @Override
    //영화 리뷰 삭제 -> 리포지토리에서 리뷰번호로 제거
    public void remove(Long reviewnum){
        reviewRepository.deleteById(reviewnum);
    }
}

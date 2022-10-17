package com.movie.boot4.repository;

import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //특정 영화의 모든 리뷰와 회원의 닉네임
    //: 특정 객체의 엔티티 속성만을 먼저 로딩하는 방법
    @EntityGraph(attributePaths = {"member"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);
}

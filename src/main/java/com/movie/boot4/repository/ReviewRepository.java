package com.movie.boot4.repository;

import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //특정 영화의 모든 리뷰와 회원의 닉네임
    List<Review> findByMovie(Movie movie);
}

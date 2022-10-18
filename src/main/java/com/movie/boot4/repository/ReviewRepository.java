package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //특정 영화의 모든 리뷰와 회원의 닉네임
    //: 특정 객체의 엔티티 속성만을 먼저 로딩하는 방법
    @EntityGraph(attributePaths = {"member"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    //회원 삭제시 리뷰 먼저 삭제 후 회원 삭제하는 메서드
    //: 비효율적인 쿼리를 개선하기 위한 어노테이션 필요 -> update/delete할 경우 필요
    //-> 해당 회원의 리뷰를 한번에 삭제하는 쿼리
    @Modifying
    @Query("delete from Review mr where mr.member=:member ")
    void deleteByMember(Member member);
}

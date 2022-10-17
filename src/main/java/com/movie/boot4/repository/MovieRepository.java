package com.movie.boot4.repository;

import com.movie.boot4.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    //영화와 리뷰를 이용한  페이징 처리
    @Query("select min(r.movie), avg(NVL(r.grade, 0)), count (distinct r) from Movie m "
    + "left outer join Review r on r.movie=m group by m")

    Page<Object[]>  getListPage(Pageable pageable);
}

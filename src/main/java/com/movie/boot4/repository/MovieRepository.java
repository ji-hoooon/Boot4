package com.movie.boot4.repository;

import com.movie.boot4.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    //영화와 리뷰를 이용한  페이징 처리
    //영화 기준
    @Query("select min(r.movie), min(mi), avg(NVL(r.grade, 0)), count (distinct r) from Movie m "
            //영화 이미지 외부 조인
            +"left outer join MovieImage mi on mi.movie =m "
            //나중에 입력된 이미지를 선택하는 쿼리
            +"and mi.inum=(select max(i2.inum) from MovieImage i2 where i2.movie=m)"
            //리뷰 외부 조인 + 영화 기준 그룹바이
            + "left outer join Review r on r.movie=m group by m")

    Page<Object[]>  getListPage(Pageable pageable);

    //특정 영화 조회 처리
    @Query("select min(mi.movie),min(mi), avg(NVL(r.grade, 0)), count(r) from Movie m "
            //리뷰 테이블과 외부조인
            +"left outer join Review r on r.movie =m "
            //영화이미지 테이블과 외부조인
            +"left outer join MovieImage mi on mi.movie = m where mi.movie.mno = :mno "
            +"group by mi ")
    List<Object[]> getMovieWithAll(Long mno);
}

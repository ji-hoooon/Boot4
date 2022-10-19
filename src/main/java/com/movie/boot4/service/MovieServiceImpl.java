package com.movie.boot4.service;

import com.movie.boot4.dto.MovieDTO;
import com.movie.boot4.dto.PageRequestDTO;
import com.movie.boot4.dto.PageResultDTO;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.MovieImage;
import com.movie.boot4.repository.MovieImageRepository;
import com.movie.boot4.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
//final을 항상 의존성 주입하는 어노테이션
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;


    //영화 객체와 영화 이미지 객체 모두 함꼐 등록해야하기 때문에
    @Transactional
    @Override
    public Long register(MovieDTO movieDTO){
        Map<String, Object> entityMap= dtoToEntity(movieDTO);
        //Map에 저장된 movie 객체
        Movie movie = (Movie) entityMap.get("movie");
        //Map에 저장된 imglist 리스트
        List<MovieImage> movieImageList=(List<MovieImage>) entityMap.get("imgList");

        //영화 엔티티 저장 후
        movieRepository.save(movie);

        //forEach문과 스트림을 이용해
        //: 영화 이미지 엔티티 저장
        movieImageList.forEach(movieImage -> {
            imageRepository.save(movieImage);
        });

        return null;
    }

    //getList() 구현
    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO){
        //requestDTO에서 페이징 메서드로 mno기준 정렬 수행해 페이징 객체에 저장
        Pageable pageable = requestDTO.getPageable(Sort.by("mno")
                .descending());

        //페이징 객체를 전달해 영화 목록을 받아서 Page에 배열로 저장
        Page<Object[]> result = movieRepository.getListPage(pageable);


        log.info("==============================================");
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
        //배열에 있는 값으로 엔티티를 가져와 DTO 만들기
        Function<Object[], MovieDTO>fn =(arr ->
                entitiesToDTO(
                        (Movie) arr[0],
                        (List<MovieImage>) (Arrays.asList((MovieImage)arr[1])),
                        (Double) arr[2],
                        (Long) arr[3])
                );
        return new PageResultDTO<>(result, fn);
    }

    //리포지토리에서 데이터를 받아서 가공해 MovieDTO를 반환하는 메서드를 구현
    @Override
    public MovieDTO getMovie(Long mno){
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        //영화 이미지를 제외하고 모든 row가 동일한 값을 가지고 있다.

        Movie movie = (Movie) result.get(0)[0]; //맨 앞에 존재하는 Movie 엔티티
        List<MovieImage> movieImageList = new ArrayList<>(); //영화 이미지 개수만큼 객체 필요
        result.forEach(arr ->{
            MovieImage movieImage=(MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg= (Double) result.get(0)[2]; //평균 평점
        Long reviewCnt = (Long) result.get(0)[3]; //리뷰 개수

        return entitiesToDTO(movie,movieImageList,avg,reviewCnt);
    }
}

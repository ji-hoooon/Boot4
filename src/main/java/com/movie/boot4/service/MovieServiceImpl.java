package com.movie.boot4.service;

import com.movie.boot4.dto.MovieDTO;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.MovieImage;
import com.movie.boot4.repository.MovieImageRepository;
import com.movie.boot4.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
}

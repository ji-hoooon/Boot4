package com.movie.boot4.service;

import com.movie.boot4.dto.MovieDTO;
import com.movie.boot4.dto.MovieImageDTO;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    //영화 등록을 위한 메서드
    Long register(MovieDTO movieDTO);

    //영화를 JPA로 처리하기위한 메서드
    //: Movie와 함께 포함된 MovieImage를 함께 반환 -> Map 이용
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
        //movie, movie 객체
        //imgList, List<MovieImageDTO>
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        //MovieImageDTO 처리
        //: 이미지가 존재할 경우에
        if(imageDTOList !=null && imageDTOList.size()>0){
            List<MovieImage> movieImageList=imageDTOList.stream()
                    .map(movieImageDTO -> {
                       MovieImage movieImage=MovieImage.builder()
                               .path(movieImageDTO.getPath())
                               .imgName(movieImageDTO.getImgName())
                               .uuid(movieImageDTO.getUuid())
                               .movie(movie)
                               .build();
                       return movieImage;
                    }).collect(Collectors.toList());

            entityMap.put("imgList",movieImageList);
        }
        return entityMap;
    }

}

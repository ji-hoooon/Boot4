package com.movie.boot4.repository;

import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.MovieImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    //테스트를 위한 의존성 주입
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository movieImageRepository;

    //영화와 영화 이미지는 같은 시점에 삽입되어야 하므로
    //(1) Movie 객체를 먼저 save
    //(2) Movie 객체에 해당하는 PK를 이용해 영화 이미지들을 추가
    //(3) 영화 이미지는 최대 5개까지 임의로 저장 [특정 영화 이미지는 많을 수 있으므로 임의의 수로 처리]
    //트랜잭션이 끝나면 처리하도록 해야 하고, 커밋을 수행하도록 테스트 작성
    @Commit
    @Transactional
    @Test
    public void insertMovies(){
        //스트림과 빌더패턴을 이용한 영화 객체 생성
        IntStream.rangeClosed(1,100).forEach(i -> {
            //100까지 포함
            Movie movie = Movie.builder()
                    .title("Movie....."+i)
                    .build();
            System.out.println("-------------------");

            movieRepository.save(movie);

            int count = (int) (Math.random() *5)+1;
            //1,2,3,4

            for(int j=0;j<count;j++){
                MovieImage movieImage=MovieImage.builder()
                        //UUID 클래스를 이용한 고유한 번호를 만들고 -> toString
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test"+j+".jpg")
                        .build();
                movieImageRepository.save(movieImage);
                System.out.println("====================================");
            }

        });
    }

    @Test
    public void testListPage(){
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result=movieRepository.getListPage(pageRequest);

        for(Object[] objects : result.getContent()){
            System.out.println(Arrays.toString(objects));
        }
    }
}

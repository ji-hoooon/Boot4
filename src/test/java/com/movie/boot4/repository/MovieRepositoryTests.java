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

    //�׽�Ʈ�� ���� ������ ����
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository movieImageRepository;

    //��ȭ�� ��ȭ �̹����� ���� ������ ���ԵǾ�� �ϹǷ�
    //(1) Movie ��ü�� ���� save
    //(2) Movie ��ü�� �ش��ϴ� PK�� �̿��� ��ȭ �̹������� �߰�
    //(3) ��ȭ �̹����� �ִ� 5������ ���Ƿ� ���� [Ư�� ��ȭ �̹����� ���� �� �����Ƿ� ������ ���� ó��]
    //Ʈ������� ������ ó���ϵ��� �ؾ� �ϰ�, Ŀ���� �����ϵ��� �׽�Ʈ �ۼ�
    @Commit
    @Transactional
    @Test
    public void insertMovies(){
        //��Ʈ���� ���������� �̿��� ��ȭ ��ü ����
        IntStream.rangeClosed(1,100).forEach(i -> {
            //100���� ����
            Movie movie = Movie.builder()
                    .title("Movie....."+i)
                    .build();
            System.out.println("-------------------");

            movieRepository.save(movie);

            int count = (int) (Math.random() *5)+1;
            //1,2,3,4

            for(int j=0;j<count;j++){
                MovieImage movieImage=MovieImage.builder()
                        //UUID Ŭ������ �̿��� ������ ��ȣ�� ����� -> toString
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

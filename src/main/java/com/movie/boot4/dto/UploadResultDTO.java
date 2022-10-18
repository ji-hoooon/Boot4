package com.movie.boot4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
//직렬화 인터페이스 구현
//: 객체 데이터를 바이트 형태로 반환해 JSON으로 결과 데이터를 반환하기 위해서
//-> 직렬화 대상은 필드만 적용되며, 생성자와 메서드는 대상에 포함되지 않는다.
//JPA 엔티티를 사용한다면, 기본값으로 직렬화가 적용되어 있는것을 알 수 있다.

public class UploadResultDTO implements Serializable {
    private String fileName;
    private String uuid;
    private String folderPath;

    //해당 DTO는 실제 파일과 관련된 모든 정보를 가지는데, 나중에 전체 경로가 필요할 경우 사용하기 위한 메서드
    //: 컨트롤러에서 업로드 결과 반환하기 위해 DTO를 엔티티로 반환해 사용
    public String getImageURL(){
        try{
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    //브라우저에서 섬네일 이미지 처리를 위한 메서드
    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(folderPath+"/"+"s_"+uuid+"_"+fileName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}

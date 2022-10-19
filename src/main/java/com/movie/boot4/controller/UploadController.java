package com.movie.boot4.controller;

import com.movie.boot4.dto.UploadResultDTO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//파일 업로드를 위한 클래스
@RestController
@Log4j2
public class UploadController {

    //업로드된 파일 저장 경로 설정
    @Value("${com.movie.upload.path}") //애플리케이션 설정 변수
    private String uploadPath;


    //MultipartFile 타입을 이용한 파일 사용
    //: Ajax를 이용한 파일 업로드 처리 -> 업로드 결과에 대한 화면 작성 X
    //따라서 업로드 결과는 JSON 형태로 제공
    @PostMapping("/uploadAjax")
    //ResponseEntity는 HttpHeader와 HttpBody를 포함하는 HttpEntity를 상속받아 구현한 클래스
    //즉, 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            //1. 확장자 검사
            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn("this is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                //이미지가 아닐경우 HttpRequest에 포함하는 HttpStatus 403을 포함해 반환
            }

            //실제 파일 이름이 전체 경로가 들어오기 때문에 원본 이름 처리와 실제 파일 이름 처리
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("filename: " + fileName);


            //2. 동일한 폴더에 많은 파일 방지
            //(1) 날짜 폴더 생성
            String folderPath = makeFolder();

            //(2) UUID 클래스를 이용한 고유한 파일 이름 생성
            String uuid = UUID.randomUUID().toString();

            //(3) UUID를 이용해 저장 파일 이름 중간에 "_"를 이용해 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath); //실제 원본 이미지 저장부

                //섬네일 생성하는데, 이름규칙 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);

                //섬네일 생성 메서드
                //: 경로, 해당 파일, 원하는 사이즈
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                //이미지 저장, 섬네일 생성 후, DTOList에 추가한다.
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
        //성공할 경우엔 HttpStatus OK를 담아 resultDTOList를 반환한다.
    }
    //(3) 폴더 생성 메서드 작성
    private String makeFolder(){
        String str= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    String folderPath = str.replace("/", File.separator);

    //경로에 폴더가 없을 경우 폴더 생성
    File uploadPathFolder =new File(uploadPath, folderPath);

    if(uploadPathFolder.exists()==false){
        uploadPathFolder.mkdirs();
    }
    return folderPath;

    }


    //URL로 이미지 전송을 위한 메서드
    //-> 섬네일 클릭시 원본 이미지 반환을 위한 size 인자 추가
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName , String size){
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileName : "+ srcFileName);
            File file = new File(uploadPath+File.separator+srcFileName);

            //섬네일 클릭시 원본 이미지 출력 위한 추가
            if(size !=null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
            }

            log.info("file : "+ file);

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            //: 마임 타입이란 클라이언트에게 전송된 문서의 다양성을 알려주기 위한 메커니즘 -> 올바른 마임타입 전송하도록 설정필요
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            //: 확장자를 통해 마임타입을 판단하는데, 확장자가 없으면 null을 반환한다.

            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
            //ResponseEntity에 바이트 배열로 만든 파일, 헤더, 상태코드 전달
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    //업로드된 파일 삭제를 위한 메서드
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;
        try {
            //삭제할 파일 이름으로 URL디코딩
            srcFileName= URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath+File.separator+srcFileName);

            //(1) 원본 파일 삭제
            boolean result = file.delete();

            //파일의 디렉토리를 가져오는 함수 :getParent()
            File thumbnail = new File(file.getParent(), "s_"+file.getName());

            //(2) 섬네일 파일 삭제
            result=thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        //Exception 클래스 : 사용자 실수와 같은 외적 요인으로 발생하는 예외로 예외처리 필수
        //: IOException / ClassNotFoundException
        //IOException 예외로 지정된 문자 부호화 형식을 지원하고 있지 않을때 발생
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

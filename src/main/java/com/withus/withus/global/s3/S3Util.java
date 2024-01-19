package com.withus.withus.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Util {

  @Value("${aws.s3.bucket}")
  private String bucket;

  private final AmazonS3Client amazonS3Client;


  //파일 업로드
  public String uploadFile(MultipartFile file, String classification) {
    try{
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      //파일명 중복 방지
      String filename = UUID.randomUUID()
          + "."
          + getExtension(Objects.requireNonNull(file.getOriginalFilename())
      );

      amazonS3Client.putObject(
          bucket + classification,
          filename,
          file.getInputStream(),metadata
      );
      return filename;
    } catch(IOException e){
      throw new BisException(ErrorCode.FAILED_UPLOAD_IMAGE);
    }

  }

  //파일 삭제
  public boolean deleteFile(String filename, String classification){
    amazonS3Client.deleteObject(bucket+classification,filename);
    return !amazonS3Client.doesObjectExist(bucket+classification, filename);
  }

  //이미지 접근
  public String getFileURL(String filename, String classification){
    return S3Const.S3_BASEURL  + classification + "/" + filename;
  }

  //확장자 추출
  private String getExtension(String originalFilename){
    int idx = originalFilename.lastIndexOf('.');
    return originalFilename.substring(idx+1);
  }
}

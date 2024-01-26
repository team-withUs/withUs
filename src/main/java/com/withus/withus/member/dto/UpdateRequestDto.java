package com.withus.withus.member.dto;

import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRequestDto(
    @NotBlank(message = "이름은 필수항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,8}$", message = "닉네임은 3자이상 8자이하 영대소문자, 숫자만 가능합니다.")
    String username,
    @NotBlank(message = "이메일은 필수항목입니다.")
    @Length(max = 255)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞게 작성해주세요.")
    String email,
    String tag,
    String introduction,
    MultipartFile imageFile

) {

  @Builder
  public UpdateRequestDto(
      String username,
      String email,
      String tag,
      String introduction,
      MultipartFile imageFile
  ) {
    this.username = username;
    this.email = email;
    this.tag = tag;
    this.introduction = introduction;
    this.imageFile = imageFile;
  }
}

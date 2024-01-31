package com.withus.withus.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRequestDto(
    @NotBlank(message = "이름은 필수항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "닉네임은 3자이상 12자이하 영대소문자, 숫자만 가능합니다.")
    String username,
    @NotBlank(message = "이메일은 필수항목입니다.")
    String tag,
    String introduction,
    MultipartFile imageFile

) {

  @Builder
  public UpdateRequestDto(
      String username,
      String tag,
      String introduction,
      MultipartFile imageFile
  ) {
    this.username = username;
    this.tag = tag;
    this.introduction = introduction;
    this.imageFile = imageFile;
  }

}

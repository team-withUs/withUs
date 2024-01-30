package com.withus.withus.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignupRequestDto (

    @NotBlank(message = "아이디는 필수항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 4자이상, 12자이하 영대소문자, 숫자만 가능합니다.")
    String loginname,
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^.{4,16}$", message = "비밀번호는 4자, 16자이하 이여야합니다.")
    String password,
    @NotBlank(message = "이름은 필수항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "닉네임은 3자이상 12자이하 영대소문자, 숫자만 가능합니다.")
    String username,
    @NotBlank(message = "이메일은 필수항목입니다.")
    @Length(max = 255)
//    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞게 작성해주세요.")
    @Email
    String email,
    @NotBlank(message = "인증코드는 필수항목입니다.")
    @Length(max = 6)
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 6자리 숫자입니다.")
    String code

) {

}

package com.withus.withus.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordRequestDto(
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^.{4,16}$", message = "비밀번호는 4자 이상, 16자 이하만 가능합니다.")
    String password
) {

}

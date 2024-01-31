package com.withus.withus.global.response.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.withus.withus.global.slack.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler(BisException.class)
    public ResponseEntity<ExceptionResponseDto> bisExceptionHandler(BisException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e
    ) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(BAD_REQUEST).body(
                new ExceptionResponseDto(BAD_REQUEST, msg)
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponseDto> handleMaxUploadSizeExceeded() {

        return ResponseEntity
            .status(ErrorCode.OVER_FILE_SIZE.getStatus())
            .body(new ExceptionResponseDto(ErrorCode.OVER_FILE_SIZE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request){
        slackService.sendSlackAlertLog(e,request);
        return ResponseEntity.status(500).body(ErrorCode.INTERNAL_SERVER_ERROR.getMsg());
    }
}

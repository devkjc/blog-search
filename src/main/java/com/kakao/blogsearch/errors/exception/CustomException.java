package com.kakao.blogsearch.errors.exception;

import com.kakao.blogsearch.errors.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

}

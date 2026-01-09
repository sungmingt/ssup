package com.ssup.backend.support;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestController
@RequestMapping("/test-exception")
public class TestExceptionController {

    @GetMapping("/business")
    public void business() {
        throw new SsupException(ErrorCode.USER_NOT_FOUND);
    }

    @GetMapping("/runtime")
    public void runtime() {
        throw new RuntimeException("boom");
    }

    @GetMapping("/method-not-supported")
    public void methodNotSupported() {
    }

    @PostMapping("/method-not-supported")
    public void postOnly() {}

    @GetMapping("/max-upload")
    public void maxUpload() {
        throw new MaxUploadSizeExceededException(1L);
    }
}

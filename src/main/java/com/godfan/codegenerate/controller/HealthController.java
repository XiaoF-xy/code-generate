package com.godfan.codegenerate.controller;

import com.godfan.codegenerate.common.BaseResponse;
import com.godfan.codegenerate.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping("/")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}

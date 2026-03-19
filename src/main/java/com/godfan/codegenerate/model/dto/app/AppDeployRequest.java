package com.godfan.codegenerate.model.dto.app;

import lombok.Data;

/**
 * 应用部署请求
 */
@Data
public class AppDeployRequest {
    /**
     * 应用 id
     */
    private Long AppId;

    private static final long serialVersionUID = 1L;
}

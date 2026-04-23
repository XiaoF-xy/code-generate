package com.godfan.codegenerate.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ProjectDownloadService {
    void downloadProjectAsZip(String projectPath, String downLoadName, HttpServletResponse httpServletResponse);
}

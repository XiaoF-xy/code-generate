package com.godfan.codegenerate.langgraph4j.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.godfan.codegenerate.langgraph4j.model.ImageResource;
import com.godfan.codegenerate.langgraph4j.model.enums.ImageCategoryEnum;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片收集工具（插画图片）
 * 保留原有类名，底层搜索源改为 Openverse，避免依赖不稳定的网页抓取结构
 */
@Slf4j
@Component
public class UndrawIllustrationTool {

    private static final String OPENVERSE_API_URL =
            "https://api.openverse.org/v1/images/?q=%s&extension=svg&page_size=%d&mature=false";
    private static final int SEARCH_COUNT = 12;

    @Tool("搜索插画图片，用于网站美化和装饰")
    public List<ImageResource> searchIllustrations(@P("搜索关键词") String query) {
        if (StrUtil.isBlank(query)) {
            return List.of();
        }

        List<ImageResource> imageList = new ArrayList<>();
        String normalizedQuery = query.trim();

        // 优先使用“插画语义”查询，如果没有结果再退回原始关键词
        List<String> candidateQueries = List.of(
                normalizedQuery + " illustration",
                normalizedQuery + " vector",
                normalizedQuery
        );

        for (String candidateQuery : candidateQueries) {
            String apiUrl = String.format(
                    OPENVERSE_API_URL,
                    URLEncoder.encode(candidateQuery, StandardCharsets.UTF_8),
                    SEARCH_COUNT
            );
            List<ImageResource> results = requestOpenverse(apiUrl);
            if (!results.isEmpty()) {
                return results;
            }
        }

        return imageList;
    }

    private List<ImageResource> requestOpenverse(String apiUrl) {
        List<ImageResource> imageList = new ArrayList<>();
        try (HttpResponse response = HttpRequest.get(apiUrl).timeout(10000).execute()) {
            if (!response.isOk()) {
                log.warn("Openverse 请求失败，status: {}, url: {}", response.getStatus(), apiUrl);
                return imageList;
            }

            JSONObject result = JSONUtil.parseObj(response.body());
            JSONArray results = result.getJSONArray("results");
            if (results == null || results.isEmpty()) {
                return imageList;
            }

            int actualCount = Math.min(SEARCH_COUNT, results.size());
            for (int i = 0; i < actualCount; i++) {
                JSONObject image = results.getJSONObject(i);
                String title = image.getStr("title", "插画");
                String url = firstNonBlank(
                        image.getStr("url"),
                        image.getStr("thumbnail"),
                        image.getStr("creator_url")
                );
                if (StrUtil.isBlank(url)) {
                    continue;
                }

                imageList.add(ImageResource.builder()
                        .category(ImageCategoryEnum.ILLUSTRATION)
                        .description(StrUtil.blankToDefault(title, "插画"))
                        .url(url)
                        .build());
            }
        } catch (Exception e) {
            log.error("搜索插画失败：{}", e.getMessage(), e);
        }
        return imageList;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
        }
        return "";
    }
}

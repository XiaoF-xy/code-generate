package com.godfan.codegenerate.service;

import com.godfan.codegenerate.model.dto.chathistory.ChatHistoryQueryRequest;
import com.godfan.codegenerate.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.godfan.codegenerate.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author GodFan
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 添加对话历史
     *
     * @param appId      应用 id
     * @param message    消息
     * @param messageType 消息类型
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);
    /**
     * 根据应用 id 删除对话历史
     *
     * @param appId 应用 id
     * @return 是否成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 分页查询某app到对话记录
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);
    /**
     * 加载历史对话到内存
     * @param appId
     * @param chatMemory
     * @param maxCount 加载最大
     * @return 加载数量
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 构造查询条件
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}

package com.github.an001code.blog.service;

import com.github.an001code.blog.utils.UserContext;
import com.github.an001code.blog.vo.ChatEventVO;
import reactor.core.publisher.Flux;

public interface ChatService {

    public static String getConversationId(String sessionId) {
        return UserContext.getUser()+"_"+sessionId;
    }

    /**
     * 聊天
     *
     * @param question  问题
     * @param sessionId 会话id
     * @return 回答内容
     */
    Flux<ChatEventVO> chat(String question, String sessionId);

    void stop(String sessionId);


}
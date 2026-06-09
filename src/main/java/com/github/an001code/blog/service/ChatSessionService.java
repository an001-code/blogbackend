package com.github.an001code.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.an001code.blog.pojo.ChatSession;
import com.github.an001code.blog.vo.ChatSessionVO;
import com.github.an001code.blog.vo.MessageVO;
import com.github.an001code.blog.vo.SessionVO;

import java.util.List;
import java.util.Map;

public interface ChatSessionService extends IService<ChatSession> {

    /**
     * 创建会话session
     *
     * @param num 热门问题的数量
     * @return 会话信息
     */
    SessionVO createSession(Integer num);

    List<SessionVO.Example> hotExamples(Integer num);

    List<MessageVO> queryBySessionId(String sessionId);

    void update(String sessionId,Long userId,String title);

    Map<String, List<ChatSessionVO>> queryHistorySession();

    void deleteHistorySession(String sessionId);

    void updateTitle(String sessionId, String title);
}
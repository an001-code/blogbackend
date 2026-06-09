package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.ChatSessionService;
import com.github.an001code.blog.vo.ChatSessionVO;
import com.github.an001code.blog.vo.MessageVO;
import com.github.an001code.blog.vo.SessionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final ChatSessionService chatSessionService;

    /**
     * 新建会话
     */
    @PostMapping
    public Result createSession(@RequestParam(value = "n", defaultValue = "3") Integer num) {
        SessionVO sess = this.chatSessionService.createSession(num);
        return Result.success(sess);
    }

    /**
     * 返回热门问题
     * @param num
     * @return
     */
    @GetMapping("/hot")
    public Result hotExamples(@RequestParam(value = "n",defaultValue = "3") Integer num){
        return Result.success(this.chatSessionService.hotExamples(num));
    }

    /**
     * 通过sessionId查询历史的对话记录
     * @param sessionId
     * @return
     */
    @GetMapping("/{sessionId}")
    public Result queryBySessionId(@PathVariable("sessionId") String sessionId){
        return Result.success(chatSessionService.queryBySessionId(sessionId));
    }

    /**
     * 返回历史记录列表
     * @return
     */
    @GetMapping("/history")
    public Result queryHistorySession(){

        return Result.success(chatSessionService.queryHistorySession());
    }

    /**
     * 删除历史会话列表
     */
    @DeleteMapping("/history")
    public void deleteHistorySession(@RequestParam("sessionId") String sessionId) {
        this.chatSessionService.deleteHistorySession(sessionId);
    }

    /**
     * 更新标题
     * @param sessionId
     * @param title
     */
    @PutMapping("/history")
    public void updateTitle(@RequestParam("sessionId") String sessionId,@RequestParam("title") String title){
        this.chatSessionService.updateTitle(sessionId,title);
    }
}
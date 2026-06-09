package com.github.an001code.blog.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
@ConfigurationProperties(prefix = "nuoma.ai.prompt.system")
public class SystemPromptConfig {

    private String chatDataId;
    private Long timeoutMs;
    private String promptContent; // 存储读取的文件内容

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void loadPromptContent() throws IOException {
        // 从 classpath 读取文件
        var resource = resourceLoader.getResource("classpath:" + chatDataId);
        if (resource.exists()) {
            promptContent = new String(resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);
        } else {
            throw new FileNotFoundException("Prompt file not found: " + chatDataId);
        }
    }

    // getters and setters
    public String getChatDataId() { return chatDataId; }
    public void setChatDataId(String chatDataId) { this.chatDataId = chatDataId; }

    public Long getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(Long timeoutMs) { this.timeoutMs = timeoutMs; }

    public String getPromptContent() { return promptContent; }
}
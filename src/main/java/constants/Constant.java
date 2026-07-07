package constants;

public interface Constant {
    public String REQUEST_ID = "requestId";
    String USER_ID = "userId";
    String STOP = "STOP";
    String ID = "id";

    interface Tools{

        String CREATE_BLOG_BY_ID = "创建一篇新的博客文章草稿，需要提供标题、正文和摘要。仅当用户明确要求写博客时才调用";
        String UPDATE_BLOG_BY_TITLE = "根据博客标题查找并更新文章内容和摘要。需要用户提供准确的标题";
        String SEARCH_ARTICLES = "根据主题或关键词从站内知识库搜索真实存在的文章，返回前3篇相关结果";
    }

    interface ToolParams{

    }
}
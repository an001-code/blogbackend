package constants;

public interface Constant {
    public String REQUEST_ID = "requestId";
    String USER_ID = "userId";
    String STOP = "STOP";
    String ID = "id";

    interface Tools{

        String CREATE_BLOG_BY_ID = "通过用户id来新建博客";
        String UPDATE_BLOG_BY_TITLE = "根据博客标题修改文章内容";
    }

    interface ToolParams{

    }
}
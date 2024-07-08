package handler;

public class PostHandler
{
    private PostHandler() {}

    private static class LazyHolder{
        private static final PostHandler INSTANCE = new PostHandler();
    }
    public static PostHandler getInstance()
    {
        return LazyHolder.INSTANCE;
    }
}

package cn.zuo.constant;

public class RedisConstants {
    // Key前缀
    public static final String USER_SESSION_PREFIX = "user:session:";
    public static final String USER_CACHE_PREFIX = "user:cache:";
    public static final String AI_CHAT_PREFIX = "ai:chat:";
    public static final String TOKEN_PREFIX = "token:";
    public static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    // 登录计数器相关
    public static final String ONLINE_USERS_KEY = "online:users";
    public static final String DAILY_LOGIN_COUNT_KEY = "login:count:daily";

    // 过期时间（秒）
    public static final int SESSION_EXPIRE = 1800;  // 30分钟
    public static final int CACHE_EXPIRE = 3600;    // 1小时
    public static final int DAILY_EXPIRE = 86400; // 24小时

    // 系统统计相关
    public static final String TOTAL_USERS_KEY = "data:total:users";
    public static final String TOTAL_DISCUSSIONS_KEY = "data:total:discussions";
    public static final String NEW_USERS_KEY = "data:new:users";
    public static final String NEW_DISCUSSIONS_KEY = "data:new:discussions";
    public static final String NEW_REPLIES_KEY = "data:new:replies";
    public static final String CATEGORY_STATS_KEY = "data:stats:categories";
}
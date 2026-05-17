package cn.zuo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author ZuoYuXiang
 * @version 1.0
 * @since 2026-02-12
 */
@SpringBootApplication
public class CampusForumApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(CampusForumApplication.class, args);
    }
}

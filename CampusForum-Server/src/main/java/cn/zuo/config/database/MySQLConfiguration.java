package cn.zuo.config.database;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.chat.memory.repository.jdbc.mysql")
@Setter
public class MySQLConfiguration {
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;

    @Bean
    public MysqlChatMemoryRepository mysqlChatMemoryRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return MysqlChatMemoryRepository.mysqlBuilder()
                .jdbcTemplate(new JdbcTemplate(dataSource))
                .build();
    }
}

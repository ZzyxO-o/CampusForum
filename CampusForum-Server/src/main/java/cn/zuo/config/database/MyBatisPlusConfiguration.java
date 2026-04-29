package cn.zuo.config.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 *
 * 配置MyBatis-Plus框架的相关设置，包括Mapper扫描和数据源配置。
 *
 * @author AI Learning Partner Team
 * @version 1.0
 * @since 2026-04-12
 */
@Configuration
@MapperScan("cn.zuo.mapper")
public class MyBatisPlusConfiguration {

    /**
     * 配置说明：
     * - @MapperScan注解自动扫描指定包下的所有Mapper接口
     * - 当前使用简化配置，不包含额外的插件
     * - 未来可以添加分页插件、性能分析插件、乐观锁插件等
     */

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
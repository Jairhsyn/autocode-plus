package tech.washmore.autocodeplus;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2019, Washmore All Rights Reserved.
 * @since 2019/4/14
 */
@SpringBootApplication(exclude = {
        MybatisAutoConfiguration.class,
        DataSourceAutoConfiguration.class
})
public class AppStarter {
    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class);
    }
}

package tech.washmore.autocodeplus.common.db;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import tech.washmore.autocodeplus.common.result.exceptions.AccessDeniedException;
import tech.washmore.autocodeplus.common.result.exceptions.InvalidParamException;
import tech.washmore.autocodeplus.model.param.Db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbConfig {
    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
    @Autowired
    private ApplicationContext applicationContext;
    private DataSource dataSource;
    private Db dbconfig = new Db();

    public Db initConfig(Db dbconfig) {
        if (checkConnection()) {
            //关闭现有的数据库连接
            applicationContext.getBean(HikariDataSource.class).close();
            //注销bean
            defaultListableBeanFactory.destroySingleton("datasource");
            autowireCapableBeanFactory.destroyBean(this.dataSource);
        }
        this.dataSource = DataSourceBuilder.create()
                .url(dbconfig.getUrl())
                .username(dbconfig.getUsername())
                .password(dbconfig.getPassword())
                .build();

        if (checkConnection()) {
            defaultListableBeanFactory.registerSingleton("datasource", this.dataSource);
            autowireCapableBeanFactory.autowireBean(this.dataSource);
            return this.dbconfig = dbconfig;
        }
        throw new InvalidParamException(1, "请输入正确的数据库配置");
    }

    private boolean checkConnection() {
        if (this.dataSource == null) {
            return false;
        }
        try (Connection connection = this.dataSource.getConnection()) {
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public DataSource getDataSource() {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        throw new AccessDeniedException("请先初始化数据库配置!");
    }

    public Connection getConnection() {
        if (this.dataSource != null) {
            try {
                return this.dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new AccessDeniedException("请先初始化数据库配置!");
    }


}

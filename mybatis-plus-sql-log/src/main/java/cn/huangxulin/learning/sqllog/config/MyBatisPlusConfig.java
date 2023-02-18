package cn.huangxulin.learning.sqllog.config;

import cn.huangxulin.learning.sqllog.mybatisplus.CommonMetaObjectHandler;
import cn.huangxulin.learning.sqllog.mybatisplus.SqlLoggerInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author hxl
 */
@Configuration
@MapperScan("cn.huangxulin.learning.sqllog.mapper")
public class MyBatisPlusConfig {

    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired
    public void setSqlSessionFactoryList(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Bean
    CommonMetaObjectHandler commonMetaObjectHandler() {
        return new CommonMetaObjectHandler();
    }

    @PostConstruct
    public void registerCustomInterceptor() {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(new SqlLoggerInterceptor(configuration));

            MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
            PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.H2);
            mybatisPlusInterceptor.addInnerInterceptor(pageInterceptor);
            configuration.addInterceptor(mybatisPlusInterceptor);
        }
    }
}

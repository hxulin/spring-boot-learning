package cn.huangxulin.learning.sqllog.mybatisplus;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.SINGLE_QUOTE;

/**
 * @author hxl
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class SqlLoggerInterceptor implements Interceptor {

    private final Configuration configuration;

    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(
            () -> new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObjectHandler = SystemMetaObject.forObject(statementHandler);
        String sqlId = ((MappedStatement) metaObjectHandler.getValue("delegate.mappedStatement")).getId();
        BoundSql boundSql = statementHandler.getBoundSql();
        long startTime = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        long sqlCost = System.currentTimeMillis() - startTime;
        showSql(sqlId, boundSql, sqlCost);
        return returnValue;
    }

    private void showSql(String sqlId, BoundSql boundSql, long sqlCost) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 替换空格、换行、tab缩进等
        String sql = boundSql.getSql().replaceAll("\\s+", StringPool.SPACE);

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        /*
         * @see org.apache.ibatis.scripting.defaults.DefaultParameterHandler 参考 Mybatis 参数处理
         */
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    String paramValueStr;
                    if (value instanceof String || value instanceof LocalDate) {
                        paramValueStr = SINGLE_QUOTE + value + SINGLE_QUOTE;
                    } else if (value instanceof LocalDateTime) {
                        paramValueStr = SINGLE_QUOTE + ((LocalDateTime) value).format(DATE_TIME_FORMATTER) + SINGLE_QUOTE;
                    } else if (value instanceof LocalTime) {
                        paramValueStr = SINGLE_QUOTE + ((LocalTime) value).format(TIME_FORMATTER) + SINGLE_QUOTE;
                    } else if (value instanceof Date) {
                        paramValueStr = SINGLE_QUOTE + DATE_FORMAT_THREAD_LOCAL.get().format(value) + SINGLE_QUOTE;
                    } else {
                        paramValueStr = value + StringPool.EMPTY;
                    }
                    // java.lang.IllegalArgumentException: Illegal group reference
                    // https://github.com/WangJi92/mybatis-sql-log/issues/4
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(paramValueStr));
                }
            }
        }
        log.info("Time: {}ms - ID：{}\n==> Execute SQL: {}", sqlCost, sqlId, sql);
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }
}

/*
 * Copyright 2019 ukuz90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.mybatis;

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnit;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnitManager;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.CrudType;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author ukuz90
 * @since 2019-06-05
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class})
})
public class CrudTypeDetectInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(CrudTypeDetectInterceptor.class);

    private static final String UPDATE_METHOD = "update";
    private static final String QUERY_METHOD = "query";
    private static final String QUERY_CURSOR_METHOD = "queryCursor";

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        //探测CRUD类型
        switch (invocation.getMethod().getName()) {
            case UPDATE_METHOD:
                RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.WRITE).build());
                break;
            case QUERY_METHOD:
            case QUERY_CURSOR_METHOD:
                RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.READ).build());
                break;
            default:
                break;
        }
        if (logger.isInfoEnabled()) {
            logger.info("mybatis intercept crud's type: {}", RoutingFlashUnitManager.getData());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

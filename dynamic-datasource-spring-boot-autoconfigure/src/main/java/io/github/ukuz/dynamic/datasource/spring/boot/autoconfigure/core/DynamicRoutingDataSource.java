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
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core;

import com.zaxxer.hikari.HikariDataSource;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.DynamicDataSourceProperties;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author ukuz90
 * @since 2019-06-04
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private DynamicDataSourceProperties dataSourceProperties;
    private RoutingStrategy routingStrategy;

    private static final Logger logger = LoggerFactory.getLogger(DynamicRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        if (this.dataSourceProperties.getProperties() == null) {
            return null;
        }
        if (this.dataSourceProperties.getProperties().length == 1) {
            return this.dataSourceProperties.getProperties()[0].getName();
        }

        Set<String> dataSourceKeys = new LinkedHashSet<>();
        Stream.of(this.dataSourceProperties.getProperties())
                .forEach(ds -> dataSourceKeys.add(ds.getName()));

        String key = routingStrategy.selectDataSourceKey(dataSourceKeys);
        if (logger.isDebugEnabled()) {
            logger.debug("determineCurrentLookupKey key:{}", key);
        }
        return key;
    }

    @Override
    public void afterPropertiesSet() {
        initDataSourceMap();
        super.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() {

    }

    private void initDataSourceMap() {
        dataSourceProperties = applicationContext.getBean(DynamicDataSourceProperties.class);
        if (dataSourceProperties.getProperties() != null) {
            Map<Object, Object> targetDataSource = new HashMap<>(dataSourceProperties.getProperties().length);
            Stream.of(dataSourceProperties.getProperties())
                    .forEach(dsp -> {
                        if (dsp.getType() == null) {
                            dsp.setType(HikariDataSource.class);
                        }
                        DataSource ds = dsp.initializeDataSourceBuilder().build();
                        targetDataSource.put(dsp.getName(), ds);
                    });
            this.setTargetDataSources(targetDataSource);
        }
        routingStrategy = applicationContext.getBean(RoutingStrategy.class);
    }
}

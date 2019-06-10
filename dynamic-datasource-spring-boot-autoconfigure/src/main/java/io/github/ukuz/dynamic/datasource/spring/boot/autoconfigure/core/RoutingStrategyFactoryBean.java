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

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.DynamicDataSourceProperties;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ServiceLoader;

/**
 * @author ukuz90
 * @since 2019-06-05
 */
public class RoutingStrategyFactoryBean implements FactoryBean<RoutingStrategy>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public RoutingStrategy getObject() {
        DynamicDataSourceProperties properties = applicationContext.getBean(DynamicDataSourceProperties.class);
        RoutingStrategy routingStrategy = (RoutingStrategy) PluginLoader.getLoader(RoutingStrategy.class).getPlugin(properties.getRoutingStrategy());
        if (routingStrategy == null) {
            throw new IllegalArgumentException("RoutingStrategy must not be null, you must set RoutingStrategy spi in META-INF/ukuz");
        }
        routingStrategy.setApplicationContext(applicationContext);
        return routingStrategy;
    }

    @Override
    public Class<?> getObjectType() {
        return RoutingStrategy.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

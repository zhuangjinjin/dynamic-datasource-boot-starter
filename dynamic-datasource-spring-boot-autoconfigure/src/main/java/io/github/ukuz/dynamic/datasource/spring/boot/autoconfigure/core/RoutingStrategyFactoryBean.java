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

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy;
import org.springframework.beans.factory.FactoryBean;

import java.util.ServiceLoader;

/**
 * @author ukuz90
 * @since 2019-06-05
 */
public class RoutingStrategyFactoryBean implements FactoryBean<RoutingStrategy> {

    @Override
    public RoutingStrategy getObject() {
        ServiceLoader<RoutingStrategy> routingStrategies = ServiceLoader.load(RoutingStrategy.class);
        if (!routingStrategies.iterator().hasNext()) {
            throw new IllegalArgumentException("RoutingStrategy must not be null, you must set RoutingStrategy spi in META-INF/services");
        }
        return routingStrategies.iterator().next();
    }

    @Override
    public Class<?> getObjectType() {
        return RoutingStrategy.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

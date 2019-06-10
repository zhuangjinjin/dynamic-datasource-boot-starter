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
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy;

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnit;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnitManager;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.DynamicDataSourceProperties;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.EnhancerDataSourceProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ukuz90
 * @since 2019-06-05
 */
public class DbOperationRoutingStrategy implements RoutingStrategy {

    private ApplicationContext applicationContext;

    @Override
    public String selectDataSourceKey(Set<String> key) {
        DynamicDataSourceProperties properties = applicationContext.getBean(DynamicDataSourceProperties.class);
        RoutingFlashUnit data = RoutingFlashUnitManager.getData();
        if (data != null) {
            Set<EnhancerDataSourceProperties> set = Stream.of(properties.getProperties())
                    .filter(props ->
                        props.containCurdType(data.getCrudType())
                    ).collect(Collectors.toSet());

            if (set.isEmpty()) {
                throw new IllegalArgumentException("No DataSource support " + data.getCrudType().name() + " operator");
            }

            return set.iterator().next().getName();
        }
        if (!key.isEmpty()) {
            return key.iterator().next();
        }
        return null;
    }

    @Override
    public Set<String> selectDataSourceKey(EnhancerDataSourceProperties[] properties) {
        Set<String> keys = null;
        RoutingFlashUnit data = RoutingFlashUnitManager.getData();
        if (data != null) {
            keys = Stream.of(properties)
                    .map(props -> props.containCurdType(data.getCrudType()) ? props.getName() : null)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
        } else {
            keys = new HashSet<>();
            keys.add(properties[0].getName());
        }
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("No DataSource support " + data.getCrudType().name() + " operator");
        }
        return keys;
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

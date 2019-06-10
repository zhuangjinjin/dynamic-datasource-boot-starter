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

import java.util.Map;
import java.util.stream.Stream;

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.DynamicDataSourceProperties;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.DynamicRoutingDataSource.DetermineDataSourceEvent;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.DynamicRoutingDataSource.ClearDetermineDataSourceEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

/**
 * @author ukuz90
 * @since 2019-06-10
 */
public class MultipleDataSourceSchemaInitializer implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static boolean isSchemaInitialized;

    @Override
    public void afterPropertiesSet() {
        initializeSchema();
    }

    private void initializeSchema() {
        if (isSchemaInitialized) {
            return;
        }
        isSchemaInitialized = true;

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = applicationContext.getBean(LocalContainerEntityManagerFactoryBean.class);
        if (entityManagerFactoryBean == null) {
            return;
        }
        PersistenceUnitInfo info = entityManagerFactoryBean.getPersistenceUnitInfo();
        Map<String, Object> props = entityManagerFactoryBean.getJpaPropertyMap();
        PersistenceProvider provider = entityManagerFactoryBean.getPersistenceProvider();
        DynamicDataSourceProperties properties = applicationContext.getBean(DynamicDataSourceProperties.class);
        Stream.of(properties.getProperties()).forEach(prop -> {
            applicationContext.publishEvent(new DetermineDataSourceEvent(prop.getName()));
            provider.createContainerEntityManagerFactory(info, props);
        });
        applicationContext.publishEvent(new ClearDetermineDataSourceEvent());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

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

import java.util.List;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.Spi;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties.EnhancerDataSourceProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

/**
 * @author ukuz90
 * @since 2019-06-05
 */
@Spi
public interface RoutingStrategy extends InitializingBean, ApplicationContextAware {

    /**
     * 选出数据源的Key
     * @param key
     * @return
     */
    @Deprecated
    String selectDataSourceKey(Set<String> key);

    Set<String> selectDataSourceKey(EnhancerDataSourceProperties[] properties);
}

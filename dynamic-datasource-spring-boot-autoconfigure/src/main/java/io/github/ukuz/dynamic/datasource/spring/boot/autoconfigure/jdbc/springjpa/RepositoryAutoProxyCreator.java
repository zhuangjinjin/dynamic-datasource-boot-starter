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
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.springjpa;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.data.repository.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ukuz90
 * @since 2019-06-07
 */
public class RepositoryAutoProxyCreator extends AbstractAutoProxyCreator {

    private final MethodBeforeAdviceInterceptor beforeInterceptor = new MethodBeforeAdviceInterceptor(new PartTreeCrudTypeDetectAdvice());
    private final AfterReturningAdviceInterceptor afterInterceptor = new AfterReturningAdviceInterceptor(new PartTreeCrudTypeDetectAdvice());
    private final Set<String> PROXYED_SET = new HashSet<>();

    private Object monitor = new Object();

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        synchronized (monitor) {
            if (PROXYED_SET.contains(beanName)) {
                return bean;
            }
            if (bean instanceof Repository) {
                bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                PROXYED_SET.add(beanName);
            }
        }
        return bean;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[]{beforeInterceptor,
                afterInterceptor};
    }

    @Override
    protected Advisor[] buildAdvisors(String beanName, Object[] specificInterceptors) {
        return super.buildAdvisors(beanName, specificInterceptors);
    }
}

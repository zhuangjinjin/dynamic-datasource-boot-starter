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
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.loadbalance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ukuz90
 * @since 2019-06-10
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoadBalance.class);

    @Override
    public ServiceInfo select(List<ServiceInfo> serviceInfoList) {
        if (CollectionUtils.isEmpty(serviceInfoList)) {
            LOGGER.error("Dangerous! Can not have available service");
            throw new RuntimeException("Dangerous! Can not have available service");
        }
        if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        }
        return doSelect(serviceInfoList);
    }

    public abstract ServiceInfo doSelect(List<ServiceInfo> serviceInfoList);
}

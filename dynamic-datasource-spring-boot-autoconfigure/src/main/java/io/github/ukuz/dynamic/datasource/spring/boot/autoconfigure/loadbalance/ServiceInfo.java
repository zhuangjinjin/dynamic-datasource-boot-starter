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

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ukuz90
 * @since 2019-06-10
 */
public class ServiceInfo {

    private final String key;
    private int weight;

    private AtomicLong current = new AtomicLong();

    public ServiceInfo(String key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    public String getKey() {
        return key;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

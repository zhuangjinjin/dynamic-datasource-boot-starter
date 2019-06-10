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
package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.properties;

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.CrudType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class EnhancerDataSourceProperties extends DataSourceProperties {

    private CrudType[] crudTypes = new CrudType[0];
    private int weight = 5;

    public CrudType[] getCrudTypes() {
        return crudTypes;
    }

    public void setCrudTypes(CrudType[] crudTypes) {
        this.crudTypes = crudTypes;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean containCurdType(CrudType crudType) {
        for (CrudType ct : crudTypes) {
            if (ct == crudType) {
                return true;
            }
        }
        return false;
    }
}
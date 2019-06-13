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

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnit;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.RoutingFlashUnitManager;
import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.CrudType;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ukuz90
 * @since 2019-06-09
 */
public class PartTreeCrudTypeDetectAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        RoutingFlashUnitManager.setData(null);
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws ClassNotFoundException {
        switch (method.getName()) {
            case "findById":
            case "existsById":
            case "findAll":
            case "findAllById":
            case "count":
            case "getOne":
                RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.READ).build());
                return;
            case "deleteById":
            case "delete":
            case "deleteAll":
            case "deleteInBatch":
            case "deleteAllInBatch":
            case "save":
            case "saveAll":
            case "saveAndFlush":
                RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.WRITE).build());
                return;
            default:
                break;
        }
        PartTree partTree = new PartTree(method.getName(), getDomainClass(method));
        if (partTree.isDelete()) {
            RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.WRITE).build());
        } else {
            RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.READ).build());
        }
    }

    private Class getDomainClass(Method method) throws ClassNotFoundException {
        Assert.isTrue(Repository.class.isAssignableFrom(method.getDeclaringClass()), method.getDeclaringClass() + "must implement Repository");
        Assert.isTrue(method.getDeclaringClass().getGenericInterfaces().length > 0, method.getDeclaringClass() + "must have generic type");

        Type[] types = method.getDeclaringClass().getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = ((ParameterizedType)type);

                if (Repository.class.isAssignableFrom(getClass(pt.getRawType().getTypeName()))) {
                    Type[] repostioryPairType = pt.getActualTypeArguments();
                    Assert.isTrue(repostioryPairType.length > 0, method.getDeclaringClass() + "'must have generic type");
                    return getClass(repostioryPairType[0].getTypeName());
                }
            }
        }
        return null;
    }

    private Class getClass(String typeName) throws ClassNotFoundException {
        return findClassLoader().loadClass(typeName);
    }

    private ClassLoader findClassLoader() {
        return PartTreeCrudTypeDetectAdvice.class.getClassLoader();
    }
}

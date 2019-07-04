package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core;

import io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.jdbc.CrudType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoutingFlashUnitManagerTest {

    @DisplayName("RoutingFlashUnitManager_setData")
    @Test
    void setData() {
        RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.READ).build());
        assertEquals(CrudType.READ, RoutingFlashUnitManager.getData().getCrudType());

        RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.WRITE).build());
        assertEquals(CrudType.WRITE, RoutingFlashUnitManager.getData().getCrudType());

        RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().tag("simple").build());
        assertEquals("simple", RoutingFlashUnitManager.getData().getTag());

        RoutingFlashUnitManager.setData(null);
        assertNull(RoutingFlashUnitManager.getData());

    }

    @DisplayName("RoutingFlashUnitManager_clearData")
    @Test
    void clearData() {
        RoutingFlashUnitManager.setData(new RoutingFlashUnit.Builder().crudType(CrudType.READ).build());
        RoutingFlashUnitManager.clearData();
        assertNull(RoutingFlashUnitManager.getData());
    }
}
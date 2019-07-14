package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class HolderTest {

    private Holder<String> holder;

    @BeforeEach
    public void before() {
        holder = new Holder<>();
    }

    @DisplayName("test_setVal_One")
    @ResourceLock(value = "message", mode = ResourceAccessMode.READ_WRITE)
    @Test
    void test_setVal_One() {
        String message = "one";
        holder.setVal(message);
        assertEquals(message, holder.getVal());
    }

    @DisplayName("test_setVal_Two")
    @ResourceLock(value = "message", mode = ResourceAccessMode.READ_WRITE)
    @Test
    void test_setVal_Two() {
        String message = "two";
        holder.setVal(message);
        assertEquals(message, holder.getVal());
    }
}
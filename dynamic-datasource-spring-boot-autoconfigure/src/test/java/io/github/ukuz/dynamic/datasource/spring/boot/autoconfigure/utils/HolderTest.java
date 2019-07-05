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

    @DisplayName("setValOne")
    @ResourceLock(value = "message", mode = ResourceAccessMode.READ_WRITE)
    @Test
    void setValOne() {
        String message = "one";
        holder.setVal(message);
        assertEquals(message, holder.getVal());
    }

    @DisplayName("setValTwo")
    @ResourceLock(value = "message", mode = ResourceAccessMode.READ_WRITE)
    @Test
    void setValTwo() {
        String message = "two";
        holder.setVal(message);
        assertEquals(message, holder.getVal());
    }
}
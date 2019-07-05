package io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PluginLoaderTest {

    @DisplayName("test_getLoader_Null")
    @Test
    void test_getLoader_Null() {
        try {
            PluginLoader.getLoader(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Plugin type must not be null", e.getMessage());
        }

    }

    @DisplayName("test_getLoader_NotInterface")
    @Test
    void test_getLoader_NotInterface() {
        try {
            PluginLoader.getLoader(SimpleOutput.class);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Plugin type class io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.SimpleOutput is not an interface!", e.getMessage());
        }
    }

    @DisplayName("test_getLoader_NotSpiAnnotation")
    @Test
    void test_getLoader_NotSpiAnnotation() {
        try {
            PluginLoader.getLoader(Output.class);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Plugin type interface io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.core.Output must annotated with @Spi", e.getMessage());
        }
    }

    @DisplayName("test_getLoader")
    @Test
    void test_getLoader() {
        PluginLoader loader = PluginLoader.getLoader(Input.class);
        assertNotNull(loader);
    }

    @DisplayName("test_getPlugin_Null")
    @Test
    void test_getPlugin_Null() {
        try {
            PluginLoader.getLoader(Input.class).getPlugin("default");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not found default mapping class", e.getMessage());
        }
    }

    @DisplayName("test_getPlugin_SpecifiedKey")
    @Test
    void test_getPlugin_SpecifiedKey() {
        Input input = (Input) PluginLoader.getLoader(Input.class).getPlugin("simple");
        assertEquals("Simple", input.read());
    }

    @DisplayName("test_getPlugin_DefaultKey")
    @Test
    void test_getPlugin_DefaultKey() {
        Input input = (Input) PluginLoader.getLoader(Input.class).getPlugin("hello");
        assertEquals("Hello", input.read());
    }

}
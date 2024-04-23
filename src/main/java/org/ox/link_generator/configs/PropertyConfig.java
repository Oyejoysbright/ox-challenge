package org.ox.link_generator.configs;

import org.springframework.context.annotation.*;

/**
 *
 * @author Ayomide.Oyediran
 */
@Configuration
public class PropertyConfig {
    @Configuration
    @Profile("default")
    @PropertySource("classpath:application.properties")
    public static class DefaultProperties{}

    @Configuration
    @Profile("dev")
    @PropertySource("classpath:application-dev.properties")
    public static class DevProperties{}
    @Configuration
    @Profile("test")
    @PropertySource("classpath:application-test.properties")
    public static class TestProperties{}
}

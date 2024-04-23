package org.ox.link_generator.configs;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;

import java.util.*;
import java.util.stream.*;

@Configuration
public class PropertyValues {
    @Autowired
    private Environment env;

    public String getAppName() {
        return env.getProperty("APP_NAME");
    }

    public String getAppDescription() {
        return env.getProperty("APP_DESCRIPTION");
    }

    public String getAppVersion() {
        return env.getProperty("APP_VERSION");
    }

}

package org.ox.link_generator.configs;

import lombok.*;
import org.springframework.context.annotation.*;
import springfox.bean.validators.configuration.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.*;

import java.util.*;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@RequiredArgsConstructor
public class SwaggerConfig {
    private final PropertyValues values;

    private ApiInfo apiInfo() {
        return new ApiInfo(
                values.getAppName(),
                values.getAppDescription(),
                values.getAppVersion(),
                null,
                new Contact(
                        "Ayomide Joysbright Oyediran",
                        "https://oyejoysbright.github.io",
                        "oyediranjoysbright@gmail.com"
                ),
                null,
                null,
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.ox.link_generator"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(-1)
                .defaultModelExpandDepth(-1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }
}
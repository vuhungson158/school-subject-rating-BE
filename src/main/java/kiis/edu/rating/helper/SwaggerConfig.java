package kiis.edu.rating.helper;

import kiis.edu.rating.RatingApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static kiis.edu.rating.helper.Constant.TOKEN_HEADER;

@SuppressWarnings("unused")
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        // securitySchemeList
        List<SecurityScheme> securitySchemeList = Collections.singletonList(
                new ApiKey("JWT Token", TOKEN_HEADER, "header")
        );

        // securityContextList
        List<SecurityReference> securityReferenceList = Collections.singletonList(
                new SecurityReference("JWT Token", new AuthorizationScope[]{
                        new AuthorizationScope("global", "accessEverything")
                })
        );
        List<SecurityContext> securityContextList = Collections.singletonList(
                SecurityContext.builder().securityReferences(securityReferenceList).build()
        );

        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(securitySchemeList)
                .securityContexts(securityContextList)
                .select()
                .apis(RequestHandlerSelectors.basePackage(RatingApplication.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }

}

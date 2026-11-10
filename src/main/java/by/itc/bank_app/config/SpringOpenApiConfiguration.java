package by.itc.bank_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
public class SpringOpenApiConfiguration {

    @Value("${app.description}")
    String appDescription;
    @Value("${app.version}")
    String appVersion;

    @Bean
    @Profile("local")
    public OpenAPI customOpenApiLocal() {
        return new OpenAPI().info(new Info().title("Tracking of bank applications")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(Collections.singletonList(new Server().url("http://localhost:8081")));
    }

    @Bean
    public GroupedOpenApi apiApplications(){
        return GroupedOpenApi.builder()
                .group("applications")
                .pathsToMatch("/api/applications/**")
                .build();
    }
}

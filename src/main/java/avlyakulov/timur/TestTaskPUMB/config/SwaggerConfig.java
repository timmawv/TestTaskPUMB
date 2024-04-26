package avlyakulov.timur.TestTaskPUMB.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("Animal parsing API")
                        .description("It is test task. It is description how to use animal parsing api")
                        .version("1.0")
                        .contact(new Contact().name("Timur")));

    }
}
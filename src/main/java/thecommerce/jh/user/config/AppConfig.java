package thecommerce.jh.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class AppConfig {

    private final DataSource dataSource;
    private final EntityManager entityManager;


    public AppConfig(DataSource dataSource, EntityManager entityManager) {
        this.dataSource = dataSource;
        this.entityManager = entityManager;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()).info(
                    new Info()
                        .title("User app restful api document")
                        .description("The commerce backend developer tech Challenges")
                );
    }
}

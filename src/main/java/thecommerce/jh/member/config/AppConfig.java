package thecommerce.jh.member.config;

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
}

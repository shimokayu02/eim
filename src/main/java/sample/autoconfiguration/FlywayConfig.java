package sample.autoconfiguration;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy strategy() {
        // see https://qiita.com/shunp/items/abea7fa01e7a664c85da#flyway
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}

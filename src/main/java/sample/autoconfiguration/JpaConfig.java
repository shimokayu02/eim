package sample.autoconfiguration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class JpaConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
        hjva.setDatabase(Database.POSTGRESQL);
        hjva.setShowSql(true);
        return hjva;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setPersistenceUnitName("eim_entityManagerFactory");
        lcemfb.setDataSource(dataSource);
        lcemfb.setPackagesToScan("sample.model.entity", "sample.context.audit");
        lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
        return lcemfb;
    }

}

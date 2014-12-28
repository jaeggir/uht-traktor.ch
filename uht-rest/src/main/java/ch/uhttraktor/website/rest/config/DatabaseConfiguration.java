package ch.uhttraktor.website.rest.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"ch.uhttraktor.website.persistence"})
public class DatabaseConfiguration implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(env.getProperty("datasource.className"));
        basicDataSource.setUrl(env.getProperty("datasource.url"));
        basicDataSource.setUsername(env.getProperty("datasource.username"));
        basicDataSource.setPassword(env.getProperty("datasource.password"));
        basicDataSource.setDefaultCatalog(env.getProperty("datasource.databaseName"));
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxActive(50);
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setValidationQuery("SELECT 1");
        return basicDataSource;
    }

    public DataSource createFlywayDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(env.getProperty("datasource.className"));
        basicDataSource.setUrl(env.getProperty("datasource.url"));
        basicDataSource.setUsername(env.getProperty("flyway.username"));
        basicDataSource.setPassword(env.getProperty("flyway.password"));
        basicDataSource.setDefaultCatalog(env.getProperty("flyway.databaseName"));
        basicDataSource.setInitialSize(1);
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setValidationQuery("SELECT 1");
        return basicDataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(createFlywayDataSource());
        flyway.setBaselineOnMigrate(true);
        flyway.setValidateOnMigrate(getBoolean(env.getProperty("flyway.validateOnMigrate")));

        flyway.setOutOfOrder(true);
        flyway.setLocations("classpath:db/migration");

        return flyway;
    }

    private boolean getBoolean(String property) {
        if (property == null || !"true".equals(property.trim().toLowerCase())) {
            return false;
        }
        return true;
    }

    @Bean
    @DependsOn(value = {"flyway"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(jpaAdapter());
        entityManagerFactory.setJpaProperties(jpaProperties());
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("ch.uhttraktor.website.domain");
        return entityManagerFactory;
    }

    public JpaVendorAdapter jpaAdapter() {
        HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        jpaAdapter.setDatabasePlatform(env.getProperty("jpa.database-platform"));
        jpaAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("jpa.show_sql")));
        jpaAdapter.setDatabase(Database.valueOf(env.getProperty("jpa.database")));
        return jpaAdapter;
    }

    private Properties jpaProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.show_sql", env.getProperty("jpa.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("jpa.format_sql"));
                setProperty("hibernate.order_inserts", env.getProperty("hibernate.order_inserts"));
                setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
                setProperty("hibernate.hibernate.order_updates", env.getProperty("hibernate.order_updates"));
                setProperty("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
                setProperty("hibernate.generate_statistics", "false");
            }
        };
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}


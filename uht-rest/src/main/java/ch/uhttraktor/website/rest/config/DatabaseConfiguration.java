package ch.uhttraktor.website.rest.config;

import ch.uhttraktor.website.AppConstants;
import org.apache.commons.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${datasource.className}")
    private String datasourceClassName;

    @Value("${datasource.url}")
    private String datasourceUrl;

    @Value("${datasource.username}")
    private String datasourceUsername;

    @Value("${datasource.password}")
    private String datasourcePassword;

    @Value("${datasource.databaseName}")
    private String datasourceDatabaseName;

    @Value("${flyway.username}")
    private String flywayUsername;

    @Value("${flyway.password}")
    private String flywayPassword;

    @Value("${flyway.validateOnMigrate}")
    private String validateOnMigrate;

    @Value("${jpa.database-platform}")
    private String jpaDatabasePlatform;

    @Value("${jpa.show_sql}")
    private String jpaShowSql;

    @Value("${jpa.format_sql}")
    private String jpaFormatSql;

    @Value("${jpa.database}")
    private String jpaDatabase;

    @Value("${hibernate.order_inserts}")
    private String hibernateOrderInserts;

    @Value("${hibernate.order_updates}")
    private String hibernateOrderUpdates;

    @Value("${hibernate.jdbc.batch_size}")
    private String hibernateBatchSize;

    @Value("${hibernate.use_sql_comments}")
    private String hibernateUseSqlComments;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(datasourceClassName);
        basicDataSource.setUrl(datasourceUrl);
        basicDataSource.setUsername(datasourceUsername);
        basicDataSource.setPassword(datasourcePassword);
        basicDataSource.setDefaultCatalog(datasourceDatabaseName);
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxActive(50);
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setValidationQuery("SELECT 1");
        return basicDataSource;
    }

    public DataSource createFlywayDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(datasourceClassName);
        basicDataSource.setUrl(datasourceUrl);
        basicDataSource.setUsername(flywayUsername);
        basicDataSource.setPassword(flywayPassword);
        basicDataSource.setDefaultCatalog(datasourceDatabaseName);
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
        flyway.setValidateOnMigrate(getBoolean(validateOnMigrate));

        flyway.setOutOfOrder(true);
        if (env.acceptsProfiles(AppConstants.STAGE_DEV)) {
            flyway.setLocations("classpath:db/migration", "db/testdata");
        } else {
            flyway.setLocations("classpath:db/migration");
        }

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
        jpaAdapter.setDatabasePlatform(jpaDatabasePlatform);
        jpaAdapter.setShowSql(Boolean.parseBoolean(jpaShowSql));
        jpaAdapter.setDatabase(Database.valueOf(jpaDatabase));
        return jpaAdapter;
    }

    private Properties jpaProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
                setProperty("hibernate.show_sql", jpaShowSql);
                setProperty("hibernate.format_sql", jpaFormatSql);
                setProperty("hibernate.order_inserts", hibernateOrderInserts);
                setProperty("hibernate.jdbc.batch_size", hibernateBatchSize);
                setProperty("hibernate.hibernate.order_updates", hibernateOrderUpdates);
                setProperty("hibernate.use_sql_comments", hibernateUseSqlComments);
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


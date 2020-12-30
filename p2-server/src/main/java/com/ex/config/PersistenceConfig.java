package com.ex.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Configuration class for persistence objects.
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    /**
     * Bean to set up datasource properties for DB.
     * Please not this was modified from the original ORM example.
     * You will need to include a "db.properties" file in order to get this to work.
     * @return the Data Source
     * @throws IOException an exception if file is missing
     */
    @Bean
    public DataSource dataSource() throws IOException {
        System.out.println("Setting up datasource");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        try(InputStream inputStream = classLoader.getResourceAsStream("db.properties")){
            Properties p = new Properties();
            p.load(inputStream);

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(p.getProperty("db.url")); //Your DB url here
            ds.setUsername(p.getProperty("db.username")); //Your username here
            ds.setPassword(p.getProperty("db.password")); //Your password here
            ds.setDriverClassName(org.postgresql.Driver.class.getName()); //Postgres drive
            System.out.println(ds.getJdbcUrl());
            return ds;
        }
    }

    /**
     * Configure a session factory
     * @return
     * @throws IOException
     */
    @Bean
    public LocalSessionFactoryBean entityManager() throws IOException {
        System.out.println("Setting up Session Factory");
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource());
        sf.setPackagesToScan("com.ex.models");
        sf.setHibernateProperties(getHibernateProperties());
        return sf;
    }

    /**
     * Set up a transaction manager
     * @return
     * @throws IOException
     */
    @Bean
    public HibernateTransactionManager transactionManager() throws IOException {
        System.out.println("Setting up Transaction Manager");
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(entityManager().getObject());
        return txManager;
    }

    /**
     * Set Hibernate Properties programmatically. You will notice these are the same properties used in XML.
     * @return
     */
    private Properties getHibernateProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        return props;
    }
}

package org.outofrange.crowdsupport.spring;

import org.outofrange.crowdsupport.config.DatabaseConfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class SpringConfiguration {
    @Inject
    private DatabaseConfig databaseConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        return new DataSourceBuilder(getClass().getClassLoader()).driverClassName(databaseConfig.getDriver())
                .url(databaseConfig.getUrl())
                .username(databaseConfig.getUsername()).password(databaseConfig.getPassword()).build();
    }
}

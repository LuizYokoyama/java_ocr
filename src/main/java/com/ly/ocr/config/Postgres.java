package com.ly.ocr.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class Postgres extends AbstractR2dbcConfiguration {

    /*
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        //to set DDL statements on a schema.sql file inside the resources folder
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);


        return initializer;
    }
*/
    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        final PostgresqlConnectionConfiguration connectionConfig = PostgresqlConnectionConfiguration.builder()
                .database("ocr")
                .host("localhost")
                .password("admin")
                .port(5433)
                .username("admin")
                .build();

        return new PostgresqlConnectionFactory(connectionConfig);
    }

}

package com.zeptolab.zeptolabchatservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

@Component
public class CleanDatabase implements ApplicationRunner {

    private final Flyway flyway;

    public CleanDatabase(final Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        flyway.clean();
        flyway.migrate();
    }

}
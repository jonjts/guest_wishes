package com.jonjts.guestwishes;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        this.packages("com.jonjts.guestwishes.api");
    }
}

package com.jonjts.guestwishes.api;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("/")
public class Service {

    @GET
    public String hello(){
        return "hello";
    }
}

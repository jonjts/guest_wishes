package com.jonjts.guestwishes.api;

import com.jonjts.guestwishes.model.Wish;
import com.jonjts.guestwishes.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/wishes")
public class WishesAPI {

    @Autowired
    private WishRepository wishRepository;

    /**
     * Find all guests
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Wish> findAll(){
        return wishRepository.findAll();
    }
}

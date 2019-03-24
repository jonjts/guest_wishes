package com.jonjts.guestwishes.api;

import com.google.gson.*;
import com.jonjts.guestwishes.model.Guest;
import com.jonjts.guestwishes.model.Wish;
import com.jonjts.guestwishes.repository.GuestRepository;
import com.jonjts.guestwishes.repository.WishRepository;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Path("/guests")
public class GuestsAPI {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private Logger logger;
    @Autowired
    private Environment env;

    /**
     * Find a lodging using the places wishes for the guest
     * @param id
     * @param radios
     * @return
     */
    @GET
    @Path("/{id}/findHotel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotel(@PathParam("id") Long id, @QueryParam("radios") Integer radios) {
        if (isGuestFound(id)) {
            try {
                Double latitudeAverage = wishRepository.getLatitudeAverage(id);
                Double longitudeAverage = wishRepository.getLongitudeAverage(id);
                String location = latitudeAverage + "," + longitudeAverage;

                Request request = createResquestToMaps(radios, location);


                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();

                JsonArray results = formatMapsResponse(response);

                return Response.ok(results.toString()).build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not create wish ", e);
                return Response.status(Response.Status.BAD_REQUEST).
                        entity(e.getMessage()).
                        build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).
                entity("guest not found").build();
    }


    /**
     * Create a new guest
     * @param guest
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Guest guest) {
        try {
            Guest saved = guestRepository.save(guest);
            return Response.ok(saved).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can not create guest.", e);
            return Response.status(Response.Status.BAD_REQUEST).
                    entity(e.getMessage()).
                    build();
        }
    }

    /**
     * Add a wish to a guest
     * @param guestId
     * @param wishe
     * @return
     */
    @POST
    @Path("/{id}/wishes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWishe(@PathParam("id") Long guestId, Wish wishe) {
        if (isGuestFound(guestId)) {
            try {
                wishe.setGuest(findGuest(guestId));
                wishe = wishRepository.save(wishe);
                return Response.ok(wishe).build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not create wish ", e);
                return Response.status(Response.Status.BAD_REQUEST).
                        entity(e.getMessage()).
                        build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).
                entity("guest not found").build();
    }

    /**
     * Find the guest wishes
     * @param guestId
     * @return
     */
    @GET
    @Path("/{id}/wishes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWishes(@PathParam("id") Long guestId) {
        if (isGuestFound(guestId)) {
            try {
                List<Wish> wishes = wishRepository.getByGuestId(guestId);
                return Response.ok(wishes).build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not find wishes ", e);
                return Response.status(Response.Status.BAD_REQUEST).
                        entity(e.getMessage()).
                        build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).
                entity("guest not found").build();
    }

    /**
     * Find a single guest
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {
        return isGuestFound(id) ?
                Response.ok(findGuest(id)).build() :
                Response.status(Response.Status.BAD_REQUEST).entity("guest not found").build();
    }

    /**
     * Find all guest
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Guest> findAll() {
        return guestRepository.findAll();
    }

    private boolean isGuestFound(Long id) {
        if (id == null) {
            return false;
        }
        Optional<Guest> byId = guestRepository.findById(id);
        return byId.isPresent();
    }


    private Guest findGuest(Long id) {
        return guestRepository.findById(id).get();
    }

    /**
     * Get the google maps response and return a json array with the places
     * @param response
     * @return
     * @throws IOException
     */
    private JsonArray formatMapsResponse(okhttp3.Response response) throws IOException {
        JsonParser jsonParser = new JsonParser();
        String responseBody = response.body().string();
        JsonObject jsonObject = jsonParser.
                parse(responseBody).getAsJsonObject();
        return jsonObject.getAsJsonArray("results");
    }

    /**
     * Create a request object to google maps api
     * @param radios
     * @param location
     * @return
     */
    private Request createResquestToMaps(Integer radios, String location) {
        // Set a defoult radios if not exist
        radios = radios == null ? 1500 : radios;

        HttpUrl httpUrl = new HttpUrl.Builder().scheme("https").
                host("maps.googleapis.com").
                addPathSegment("maps").
                addPathSegment("api").
                addPathSegment("place").
                addPathSegment("nearbysearch").
                addPathSegment("json").
                addQueryParameter("location", location).
                addQueryParameter("radius", String.valueOf(radios)).
                addQueryParameter("type", "lodging").
                addQueryParameter("key", env.getProperty("maps_key")).
                build();

        return new Request.Builder().
                url(httpUrl).
                get().
                build();
    }

}

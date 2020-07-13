package com.newmanconnection.hello.rest;

import com.newmanconnection.hello.Hello;
import com.newmanconnection.hello.db.HelloDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.rest.PaginatedResponse;
import org.servantscode.commons.rest.SCServiceBase;

import javax.ws.rs.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/hello")
public class HelloSvc extends SCServiceBase {
    private static final Logger LOG = LogManager.getLogger(HelloSvc.class);

    private HelloDB db;

    public HelloSvc() {
        db = new HelloDB();
    }

    @GET @Produces(APPLICATION_JSON)
    public PaginatedResponse<Hello> getPage(@QueryParam("search") String search,
                                             @QueryParam("sort") @DefaultValue("name") String sort,
                                             @QueryParam("start") @DefaultValue("0") int start,
                                             @QueryParam("count") @DefaultValue("20") int count) {
        return processRequest(() -> {
            int totalResults = db.getCount(search);

            List<Hello> results = db.getPage(search, sort, start, count);

            return new PaginatedResponse<>(start, count, totalResults, results);
        });
    }

    @GET @Path("/{id}") @Produces(APPLICATION_JSON)
    public Hello get(@PathParam("id") int id) {
        return processRequest(() -> {
            return db.get(id);
        });
    }

    @POST @Consumes(APPLICATION_JSON) @Produces(APPLICATION_JSON)
    public Hello create(Hello hello) {
        return processRequest(() -> {
            return db.create(hello);
        });
    }

    @PUT @Consumes(APPLICATION_JSON) @Produces(APPLICATION_JSON)
    public Hello update(Hello hello) {
        return processRequest(() -> {
            Hello updated = db.update(hello);
            if(updated == null)
                throw new NotFoundException();

            return updated;
        });
    }

    @DELETE @Path("/{id}")
    public void delete(@PathParam("id") int id) {
        Hello hello = db.get(id);
        if(hello == null)
            throw new NotFoundException();

        processRequest(() -> {
            db.delete(id);
        });
    }
}

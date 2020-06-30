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
        try {
            int totalResults = db.getCount(search);

            List<Hello> results = db.getPage(search, sort, start, count);

            return new PaginatedResponse<>(start, count, totalResults, results);
        } catch(Throwable t) {
            LOG.error("Could not retrieve hellos", t);
            throw t;
        }
    }

    @GET @Path("/{id}") @Produces(APPLICATION_JSON)
    public Hello get(@PathParam("id") int id) {
        try {
            return db.get(id);
        } catch(Throwable t) {
            LOG.error("Could not retrieve hello by id: " + id, t);
            throw t;
        }
    }

    @POST @Consumes(APPLICATION_JSON) @Produces(APPLICATION_JSON)
    public Hello create(Hello hello) {
        try {
            return db.create(hello);
        } catch(Throwable t) {
            LOG.error("Could not create hello: " + hello.getName(), t);
            throw t;
        }
    }

    @PUT @Consumes(APPLICATION_JSON) @Produces(APPLICATION_JSON)
    public Hello update(Hello hello) {
        try {
            Hello updated = db.update(hello);
            if(updated == null)
                throw new NotFoundException();

            return updated;
        } catch(Throwable t) {
            LOG.error("Could not update hello: " + hello.getName(), t);
            throw t;
        }
    }

    @DELETE @Path("/{id}")
    public void delete(@PathParam("id") int id) {
        Hello hello = db.get(id);
        if(hello == null)
            throw new NotFoundException();

        try {
            db.delete(id);
        } catch(Throwable t) {
            LOG.error("Could not delete hello with id: " + hello.getName(), t);
            throw t;
        }
    }
}

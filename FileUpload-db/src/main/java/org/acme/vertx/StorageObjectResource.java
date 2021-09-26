package org.acme.vertx;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/objects")
public class StorageObjectResource {

    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @Inject
    @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
    boolean schemaCreate;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    @GET
    public Multi<StorageObject> get() {
        return StorageObject.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<StorageObject> getSingle(@PathParam long id) {
        return StorageObject.findById(client, id);
    }

    @POST
    public Uni<Response> create(StorageObject obj) {
        return obj.save(client)
        .onItem().transform(inserted -> inserted ? Status.ACCEPTED : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    }

    private void initdb() {
        
        client.query("CREATE TABLE IF NOT EXISTS " + StorageObject.TblName + "  (id SERIAL PRIMARY KEY, name TEXT, size int, path TEXT)")
                .execute().await().indefinitely();
    }

}
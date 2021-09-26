package org.acme.rest.client.multipart;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

import javax.enterprise.inject.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.util.Set;
import javax.ws.rs.core.MediaType;


@Path("/objects")
@RegisterRestClient
public interface StorageObjectService {

    @GET
    Set<StorageObject> GetAll();

    @GET
    @Path("{id}")
    StorageObject GetById(@PathParam("id") long id);

    @POST
    StorageObject Save(StorageObject obj);

}
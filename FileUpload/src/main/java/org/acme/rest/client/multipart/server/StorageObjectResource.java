package org.acme.rest.client.multipart.server;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import org.acme.rest.client.multipart.StorageObject;
import org.acme.rest.client.multipart.StorageObjectService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Set;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/objects")
public class StorageObjectResource {

    @Inject
    @RestClient
    StorageObjectService SOService;

    @GET
    @Blocking
    @Produces(MediaType.APPLICATION_JSON)
    public Set<StorageObject> GetAllStorageObjects() {
        return SOService.GetAll();
    }

    @GET
    @Path("{id}")
    @Blocking
    public StorageObject GetSingleStorageObject(long id) {
         return SOService.GetById(id);
     }

    @POST
    @Blocking
    public StorageObject SaveStorageObject(StorageObject obj) {
        return SOService.Save(obj);
    }

}
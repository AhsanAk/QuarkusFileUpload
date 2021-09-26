package org.acme.rest.client.multipart;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.acme.rest.client.multipart.Utilities.Utilities;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.eclipse.microprofile.rest.client.inject.RestClient;

// @Path("/C:/Program Files/Git/FileUpload")
@Path("/FileUpload")
public class FileUploadResource {

    @Inject
    @RestClient
    StorageObjectService SOService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm MultipartBody form) {

        Utilities u = new Utilities();
        String fileName = u.getRandomFileName();
        String FileExt = u.getFileExtension(form.getFileName());
        long size = 0;
        String completeFilePath = "e:/test/" + fileName + FileExt;
        try {
            // Save the file
            File file = new File(completeFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            int read = 0;
           
            byte[] bytes = new byte[1024];
            FileOutputStream fos = new FileOutputStream(file);
            while ((read = form.file.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
            size = file.length();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageObject obj = new StorageObject();
        obj.name = form.getFileName();
        obj.path = completeFilePath;
        obj.size = size;
        SOService.Save(obj);
        return Response.status(200).entity("Success: Uploaded file name : " + fileName).build();
    }

    @GET
    public Set<StorageObject> GetAllObjects() {
        return SOService.GetAll();
    }

    @GET
    @Path("{id}")
    public StorageObject GetByObjectId(@PathParam("id") long id) {
         return SOService.GetById(id);
    }




}
package org.acme.rest.client.multipart;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.acme.rest.client.multipart.Utilities.Utilities;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

// @Path("/C:/Program Files/Git/FileUpload")
@Path("/FileUpload")
public class FileUploadResource {

    @Inject
    @RestClient
    MultipartService service;

    @POST
    public Response uploadFile(@MultipartForm MultipartBody form) {

        Utilities u = new Utilities();
        String fileName = u.getRandomFileName();
        String FileExt = u.getFileExtension(form.getFileName());
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
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.status(200).entity("Success: Uploaded file name : " + fileName).build();
    }

}
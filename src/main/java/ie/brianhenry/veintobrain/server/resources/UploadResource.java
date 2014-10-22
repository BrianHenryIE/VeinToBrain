package ie.brianhenry.veintobrain.server.resources;

import ie.brianhenry.veintobrain.shared.representations.UploadResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author BrianHenry.ie
 * @see http://www.mkyong.com/webservices/jax-rs/file-upload-example-in-jersey/
 */
@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadResource {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public UploadResponse uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
//		public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
//				@FormDataParam("file") FormDataContentDisposition fileDetail) {

		System.out.println("file upload!");
		
		// String uploadedFileLocation = "./temp/" + fileDetail.getFileName();
		String uploadedFileLocation = "./"+fileDetail.getFileName();

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
			
			return new UploadResponse(false, fileDetail.getFileName());
		}

		String output = "File uploaded to : " + uploadedFileLocation;

//		return Response.status(200).entity(output).build();
		return new UploadResponse(true, fileDetail.getFileName());
	}

}

package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;


@Component
public class ResourceTemplateImpl implements ResourceOperations{

	final static File TARGET_FOLDER = new File("c:\\tmp" );;
	
	@Override
	public BufferedImage readImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to access resource: " + url , ex);
		}
	}
	
	
	public void uploadFile(final UploadedFile uploadedFile, final String targetFileName){
		
		
		try (final InputStream inputStream = uploadedFile.getInputstream(); final OutputStream out = new FileOutputStream(new File(TARGET_FOLDER , targetFileName))) {
		
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			

		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to access resource: ", ex ); 
		}

	}
	
	
}

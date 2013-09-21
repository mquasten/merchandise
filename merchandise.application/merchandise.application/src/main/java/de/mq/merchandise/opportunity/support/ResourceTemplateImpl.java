package de.mq.merchandise.opportunity.support;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;


@Component
public class ResourceTemplateImpl implements ResourceOperations{

	static final int BUFFER_SIZE = 1024;

	
	
	
	public BufferedImage readImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to access resource: " , ex);
		}
		
	}
	
	
	public int copy(final InputStream inputStream, final OutputStream os ){
		
		try  {
		   
			int read = 0;
			byte[] bytes = new byte[BUFFER_SIZE];
            int size=0;
			while ((read = inputStream.read(bytes)) != -1) {
				os.write(bytes, 0, read);
				size+=read;
			}

			os.flush();
		    return size;

		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to access resource: ", ex ); 
		}

	}



	
	
}

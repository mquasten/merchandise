package de.mq.merchandise.opportunity.support;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Encapsulates the sun/oracle static crap, in a way like it is done in spring
 * @author mquasten
 *
 */
public interface ResourceOperations {
	
	/**
	 * Get an image from a given url
	 * @param url the string for the url to the image
	 * @return the image that is related to the url  or null if it isn't an image
	 */
	BufferedImage readImage(final String url) ;
	
	
	/**
	 * Copy a file that should be uploaded to the server 
	 * @param uploadedFile  the file that should be  uploaded
	 * @param outputStream the output stream to that the input stream should be copied
	 * @ return the size of the copied data
	 */
	 int copy(final InputStream inputStream,  final OutputStream outputStream);
	
	

}

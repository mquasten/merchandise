package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;


/**
 * Encapsulates the sun/oracle static crap, in a way like it is done in spring
 * @author mquasten
 *
 */
public interface ResourceOperations {
	
	/**
	 * Get an image from a given url
	 * @param url the string of the url
	 * @return the image that is related to the url string or null if it isn't an image
	 */
	BufferedImage readImage(final String url) ;

}

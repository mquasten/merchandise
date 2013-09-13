package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;


@Component
public class ResourceTemplateImpl implements ResourceOperations{

	@Override
	public BufferedImage readImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to access resource: " + url , ex);
		}
	}

}

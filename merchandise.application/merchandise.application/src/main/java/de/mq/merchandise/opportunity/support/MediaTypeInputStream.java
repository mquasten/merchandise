package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.http.MediaType;

public interface MediaTypeInputStream {

	public abstract MediaType mediaType();

	public abstract InputStream inputStream();

}
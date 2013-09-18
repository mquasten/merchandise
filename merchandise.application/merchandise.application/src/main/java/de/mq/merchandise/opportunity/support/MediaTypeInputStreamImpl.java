package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.http.MediaType;

public class MediaTypeInputStreamImpl implements MediaTypeInputStream  {
	
    private final MediaType mediaType;
	
	private final InputStream inputStream;
	
	public MediaTypeInputStreamImpl(final InputStream inputStream, final String contentType) {
		this.mediaType = MediaType.parseMediaType(contentType);
		this.inputStream = inputStream;
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.MediaTypeInputStream#mediaType()
	 */
	@Override
	public final MediaType mediaType() {
		return this.mediaType;
	}
	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.MediaTypeInputStream#inputStream()
	 */
	@Override
	public final InputStream inputStream() {
		return inputStream;
	}
	
}

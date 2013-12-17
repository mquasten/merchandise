package de.mq.merchandise.opportunity.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import de.mq.merchandise.opportunity.ResourceOperations;


class SimpleMediaTypeInputStreamHttpMessageConverterImpl implements   HttpMessageConverter<InputStream> {
	
	
	static final MediaType MEDIA_TYPE_PDF = MediaType.parseMediaType("application/pdf");
	private final ResourceOperations resourceOperations;
	
	@Autowired
	SimpleMediaTypeInputStreamHttpMessageConverterImpl(final ResourceOperations resourceOperations) {
		this.resourceOperations=resourceOperations;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
		return  InputStream.class.isAssignableFrom(clazz) && supports(mediaType);
	}

	boolean supports(final MediaType mediaType) {
		for(MediaType m : getSupportedMediaTypes()){
			if( mediaType.isCompatibleWith(m) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		final List<MediaType> results = new ArrayList<>();
		results.add(MediaType.IMAGE_JPEG);
		results.add(MediaType.IMAGE_GIF);
		results.add(MediaType.IMAGE_PNG);
		results.add(MEDIA_TYPE_PDF);
		results.add(MediaType.APPLICATION_OCTET_STREAM);
		return results;
	}

	@Override
	public InputStream read(Class<? extends InputStream> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new HttpMessageNotReadableException("Read is not supported");
	}

	@Override
	public void write(final InputStream inputStream, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		outputMessage.getHeaders().setContentLength(resourceOperations.copy(inputStream, outputMessage.getBody()));
	}

	

	
	
	

}

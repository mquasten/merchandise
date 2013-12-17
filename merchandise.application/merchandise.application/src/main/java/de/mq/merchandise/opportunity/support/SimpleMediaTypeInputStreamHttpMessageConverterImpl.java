package de.mq.merchandise.opportunity.support;

import java.io.ByteArrayOutputStream;
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


class SimpleMediaTypeInputStreamHttpMessageConverterImpl<T> implements   HttpMessageConverter<T> {
	
	
	static final MediaType MEDIA_TYPE_PDF = MediaType.parseMediaType("application/pdf");
	private final ResourceOperations resourceOperations;
	
	@Autowired
	SimpleMediaTypeInputStreamHttpMessageConverterImpl(final ResourceOperations resourceOperations) {
		this.resourceOperations=resourceOperations;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		if( mediaType==null){
			return false;
		}
		return byte[].class.isAssignableFrom(clazz)&&supports(mediaType);
	}

	@Override
	public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
		if( mediaType==null){
			return false;
		}
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
	public void write(final T inputStream, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		outputMessage.getHeaders().setContentLength(resourceOperations.copy((InputStream)inputStream, outputMessage.getBody()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		resourceOperations.copy(inputMessage.getBody(), os);
		return (T) os.toByteArray();
	}

	

	
	
	

}

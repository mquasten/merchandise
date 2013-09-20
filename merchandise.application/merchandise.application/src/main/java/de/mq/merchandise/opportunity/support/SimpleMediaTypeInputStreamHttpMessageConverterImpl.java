package de.mq.merchandise.opportunity.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

class SimpleMediaTypeInputStreamHttpMessageConverterImpl implements   HttpMessageConverter<InputStream> {

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return  InputStream.class.isAssignableFrom(clazz);
		
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		final List<MediaType> results = new ArrayList<>();
		results.add(MediaType.IMAGE_JPEG);
		results.add(MediaType.IMAGE_GIF);
		results.add(MediaType.IMAGE_PNG);
		results.add(MediaType.parseMediaType("application/pdf"));
		return results;
	}

	@Override
	public InputStream read(Class<? extends InputStream> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new IOException("Read is not supported");
	}

	@Override
	public void write(final InputStream inputStream, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		
		//outputMessage.getHeaders().setContentType(mediaTypeInputStream.mediaType());
		int size=0;
		try ( final OutputStream out =outputMessage.getBody()) {
			
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				size+=read;
				out.write(bytes, 0, read);
			}

			out.flush();
			outputMessage.getHeaders().setContentLength(size);
			
		} 
		
	}

	

	
	
	

}

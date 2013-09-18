package de.mq.merchandise.opportunity.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.client.ResourceAccessException;

public class SimpleFileHttpMessageConverter implements   HttpMessageConverter<InputStream> {

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		System.out.println("????");
		return clazz.equals(FileInputStream.class);
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		System.out.println("????");
		final List<MediaType> results = new ArrayList<>();
		
		results.add(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
		return results;
	}

	@Override
	public InputStream read(Class<? extends InputStream> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new IOException("Read is not supported");
	}

	@Override
	public void write(final InputStream inputStream, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		
		System.out.println("***************************");
		
	  //outputMessage.getHeaders().setContentType(MediaType.IMAGE_JPEG);
		
		//outputMessage.getHeaders().setContentType(MediaType.parseMediaType(new MimetypesFileTypeMap(inputStream)));
		try ( final OutputStream out =outputMessage.getBody()) {
			
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			
System.out.println("***");
		} 
		
	}

	

	
	
	

}

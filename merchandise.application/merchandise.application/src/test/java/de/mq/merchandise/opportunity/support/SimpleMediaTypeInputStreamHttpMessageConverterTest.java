package de.mq.merchandise.opportunity.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import de.mq.merchandise.opportunity.ResourceOperations;

public class SimpleMediaTypeInputStreamHttpMessageConverterTest {
	
	
	private static final int LENGTH = 19680528;

	private final ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	
	private final HttpMessageConverter<InputStream> messageConverter = new SimpleMediaTypeInputStreamHttpMessageConverterImpl<InputStream>(resourceOperations);
	
	
	@Test
	public final void accept() {
		final List<MediaType> results = messageConverter.getSupportedMediaTypes();
		Assert.assertEquals(5, results.size());
		Assert.assertTrue(results.contains(MediaType.IMAGE_GIF));
		Assert.assertTrue(results.contains(MediaType.IMAGE_PNG));
		Assert.assertTrue(results.contains(MediaType.IMAGE_JPEG));
		Assert.assertTrue(results.contains(MediaType.APPLICATION_OCTET_STREAM));
		Assert.assertTrue(results.contains(SimpleMediaTypeInputStreamHttpMessageConverterImpl.MEDIA_TYPE_PDF));
	}
	
	@Test
	public final void canRead() {
		 Assert.assertFalse(messageConverter.canRead(InputStream.class, MediaType.IMAGE_JPEG));
	}
	
	@Test
	public final void canWrite() {
		Assert.assertTrue(messageConverter.canWrite(InputStream.class, MediaType.IMAGE_JPEG));
		Assert.assertFalse(messageConverter.canWrite(String.class, MediaType.IMAGE_JPEG));
		Assert.assertFalse(messageConverter.canWrite(InputStream.class, MediaType.APPLICATION_ATOM_XML));
	}
	
	
	
	@Test
	public final void write() throws HttpMessageNotWritableException, IOException {
		final InputStream is = Mockito.mock(InputStream.class);
		final HttpOutputMessage httpOutputMessage = Mockito.mock(HttpOutputMessage.class);
		final HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
		Mockito.when(httpOutputMessage.getHeaders()).thenReturn(httpHeaders);
		final OutputStream os = Mockito.mock(OutputStream.class);
		Mockito.when(httpOutputMessage.getBody()).thenReturn(os);
		Mockito.when(resourceOperations.copy(is, os)).thenReturn(LENGTH);
		
		messageConverter.write(is, MediaType.IMAGE_JPEG, httpOutputMessage);
		
		Mockito.verify(resourceOperations).copy(is, os);
		Mockito.verify(httpHeaders).setContentLength(LENGTH);
	}

}

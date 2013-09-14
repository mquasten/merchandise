package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.SelectableDataModel;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.model.support.SimpleMapDataModel;

public class Document2SimpleMapDataModelTest {
	
	private static final String IMAGE_KEY = "kylie.jpg";
	private Converter<DocumentsAware, SelectableDataModel<? extends Object>> converter = new Document2SimpleMapDataModel();  
	
	@Test
	public final void convert() {
		final DocumentsAware documentsAware = Mockito.mock(DocumentsAware.class);
		final Map<String,byte[]> documents = new HashMap<>();
		documents.put(IMAGE_KEY, " ".getBytes());
		Mockito.when(documentsAware.documents()).thenReturn(documents);
		
		@SuppressWarnings("unchecked")
		final List<String> results =  (List<String>) converter.convert(documentsAware);
		
		Assert.assertTrue((results instanceof SimpleMapDataModel<?>));
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(IMAGE_KEY, results.get(0));
	}

}

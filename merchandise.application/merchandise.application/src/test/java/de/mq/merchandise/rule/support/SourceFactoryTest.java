package de.mq.merchandise.rule.support;


	
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.FileCopyUtils;

import de.mq.merchandise.opportunity.support.DocumentService;
import de.mq.merchandise.rule.Validator;
import de.mq.merchandise.util.Parameter;
	
	
	public class SourceFactoryTest {
		
		private static final long ID = 19680528L;

		private final DocumentService documentService = Mockito.mock(DocumentService.class);
		
		private final SourceFactoryImpl   sourceFactory  = new SourceFactoryImpl(documentService);
		
		private final String  classWithError  = "abstract class DontLetMeGetMe  { DontLetMeGetMe() { } }";
		
		@Before
		public final void setup() throws IOException {
			Mockito.when(documentService.document(ID)).thenReturn(FileCopyUtils.copyToByteArray(new File("src/test/groovy/StringToNumberValidatorAndConverterImpl.groovy")));
		}
		
		
		@Test
		public final void create() throws FileNotFoundException {
		
			@SuppressWarnings("unchecked")
			final Parameter<Class<Integer>> parameter = Mockito.mock(Parameter.class);
			Mockito.when(parameter.name()).thenReturn("type");
			Mockito.when(parameter.value()).thenReturn(Integer.class);
			final Validator<?> result = sourceFactory.create(ID, parameter);
			
			
			Assert.assertEquals("StringToNumberValidatorAndConverterImpl", result.getClass().getSimpleName());
			
			Assert.assertEquals(1, result.parameters().length);
			
			Assert.assertEquals(Integer.class, result.getProperty(result.parameters()[0]));
		}

	
	
	
		@Test(expected=IllegalStateException.class)
		public final void createWrongSource() {
			Mockito.when(documentService.document(ID)).thenReturn(classWithError.getBytes());
			sourceFactory.create(ID);
		}


	}

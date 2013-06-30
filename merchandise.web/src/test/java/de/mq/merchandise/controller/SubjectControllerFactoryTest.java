package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.util.BasicService;

public class SubjectControllerFactoryTest {
	
	private final  SujectControllerFactoryImpl sujectControllerFactory= new SujectControllerFactoryImpl(); 
	
	private final  WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
	
	private final BasicService commercialSubjectService = Mockito.mock(CommercialSubjectService.class);
	
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(sujectControllerFactory, "webProxyFactory", webProxyFactory);
		ReflectionTestUtils.setField(sujectControllerFactory, "commercialSubjectService", commercialSubjectService);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void subjectController() {
		ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		SubjectController subjectController = Mockito.mock(SubjectController.class);
		ArgumentCaptor<SubjectControllerImpl> modellRepositoryArgumentCaptor = ArgumentCaptor.forClass(SubjectControllerImpl.class);
		Mockito.when(webProxyFactory.webModell(classCaptor.capture(),modellRepositoryArgumentCaptor.capture() )).thenReturn(subjectController);
		
		Assert.assertEquals(subjectController, sujectControllerFactory.subjectController());
		
		Assert.assertEquals(SubjectControllerImpl.class, modellRepositoryArgumentCaptor.getValue().getClass());
		Assert.assertEquals(SubjectController.class, classCaptor.getValue());
		
	}

}

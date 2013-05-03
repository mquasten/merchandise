package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.CommercialSubject.DocumentType;
import de.mq.merchandise.util.EntityUtil;

public class CommercialSubjectTest {
	private static final byte[] DOCUMENT = "http://www.dolls.de/content".getBytes();
	private static final String DOCUMENT_NAME = "escorts";
	private static final long ID = 19680528L;
	private static final String DESCRIPTION = DOCUMENT_NAME;
	private static final String NAME = "Escort-Service";

	@Test
	public final void constructor() {
		final Customer customer = Mockito.mock(Customer.class);
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		Assert.assertEquals(NAME, commercialSubject.name());
		Assert.assertEquals(DESCRIPTION, commercialSubject.description());
		Assert.assertEquals(customer, commercialSubject.customer());
	}
	
	@Test
	public final void hasId() {
		final Customer customer = Mockito.mock(Customer.class);
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		Assert.assertFalse(commercialSubject.hasId());
		ReflectionTestUtils.setField(commercialSubject, "id", ID);
		
		Assert.assertTrue(commercialSubject.hasId());
	}

	@Test
	public final void id() {
		final Customer customer = Mockito.mock(Customer.class);
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		ReflectionTestUtils.setField(commercialSubject, "id", ID);
		
		Assert.assertEquals(ID, commercialSubject.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotFound() {
		final Customer customer = Mockito.mock(Customer.class);
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		
		Assert.assertEquals(ID, commercialSubject.id());
	}
	
	@Test
	public final void assignDocument() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		
		commercialSubject.assignDocument(DOCUMENT_NAME, DocumentType.Link, DOCUMENT);
		
		@SuppressWarnings("unchecked")
		Map<String, byte[]> results = (Map<String, byte[]>) ReflectionTestUtils.getField(commercialSubject, "storedDocuments");
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(DOCUMENT_NAME,  results.keySet().iterator().next());
		Assert.assertEquals(DOCUMENT,  results.values().iterator().next());
		
	}
	
	@Test
	public final void removeDocument() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		final Map<String, byte[]> results = new HashMap<>();
		results.put(DOCUMENT_NAME, DOCUMENT);
		
		ReflectionTestUtils.setField(commercialSubject, "storedDocuments", results);
	    commercialSubject.removeDocument(DOCUMENT_NAME, DocumentType.Link);
		Assert.assertTrue(results.isEmpty());
	}  
	
	@Test
	public final void documents() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		
		final Map<String, byte[]> results = new HashMap<>();
		results.put(DOCUMENT_NAME, DOCUMENT);
		
		ReflectionTestUtils.setField(commercialSubject, "storedDocuments", results);
		
		
		Assert.assertEquals(results, commercialSubject.documents());
	
	}
	
	@Test
	public final void hash() {
		DocumentsAware commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		Assert.assertEquals(System.identityHashCode(commercialSubject), commercialSubject.hashCode() );
		
		final Customer customer = Mockito.mock(Customer.class);
		commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		Assert.assertEquals((NAME.hashCode() + customer.hashCode()), commercialSubject.hashCode()) ;
		
	}
	
	@Test
	public final void equals() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		
		Assert.assertTrue(commercialSubject.equals(commercialSubject));
		Assert.assertFalse(commercialSubject.equals(EntityUtil.create(CommercialSubjectImpl.class)));
		
		Assert.assertTrue(new CommercialSubjectImpl(customer, NAME, DESCRIPTION).equals(new CommercialSubjectImpl(customer, NAME, DESCRIPTION)));
		Assert.assertFalse(new CommercialSubjectImpl(customer, NAME, DESCRIPTION).equals(new CommercialSubjectImpl(customer, "???", DESCRIPTION)));
	}
	
}

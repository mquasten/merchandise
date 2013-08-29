package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.DocumentsAware.DocumentType;
import de.mq.merchandise.util.EntityUtil;

public class CommercialSubjectTest {
	private static final String IMAGE = "kylie.jpg";
	private static final String PATH = "artists";
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
	
	@Test
	public final void documentType() {
		for(final DocumentType documentType : DocumentType.values()){
			Assert.assertEquals(documentType, DocumentType.valueOf(documentType.name()));
		}
		Assert.assertEquals(PATH + ".pdf", DocumentType.PDF.key(PATH));
		Assert.assertEquals(PATH, DocumentType.Link.key(PATH));
	}
	
	@Test
	public final void urlForName(){
		final CommercialSubject commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		ReflectionTestUtils.setField(commercialSubject, "id" , ID);
		Assert.assertEquals(String.format(CommercialSubjectImpl.URL, ID, IMAGE ), commercialSubject.urlForName(IMAGE));
	}
	
	@Test
	public final void assignLink(){
		final CommercialSubject commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		ReflectionTestUtils.setField(commercialSubject, "id" , ID);
		commercialSubject.assignDocument(IMAGE);
		
		Assert.assertEquals(1, commercialSubject.documents().size());
		Assert.assertEquals(IMAGE, commercialSubject.documents().keySet().iterator().next());
		
		Assert.assertEquals(String.format(CommercialSubjectImpl.URL, ID, IMAGE ),new String(commercialSubject.documents().values().iterator().next()));
		
	}
	
}

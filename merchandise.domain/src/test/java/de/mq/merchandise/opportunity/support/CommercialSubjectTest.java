package de.mq.merchandise.opportunity.support;


import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.EntityUtil;

public class CommercialSubjectTest {
	private static final String WEB_LINK = "kylie.com";
	private static final String IMAGE = "kylie.jpg";

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
	public final void assignWebLink() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		
		commercialSubject.assignWebLink(WEB_LINK);
		
		@SuppressWarnings("unchecked")
		Map<String, String> results = (Map<String,String>) ReflectionTestUtils.getField(commercialSubject, "storedDocuments");
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(WEB_LINK,  results.keySet().iterator().next());
		Assert.assertEquals(String.format(CommercialSubjectImpl.WWW_URL, WEB_LINK),  new String(results.values().iterator().next()));
		
	}
	
	@Test
	public final void removeDocument() {
		final Customer customer = Mockito.mock(Customer.class);
		final DocumentsAware commercialSubject = new CommercialSubjectImpl(customer , NAME , DESCRIPTION);
		final Map<String, byte[]> results = new HashMap<>();
		results.put(DOCUMENT_NAME, DOCUMENT);
		
		ReflectionTestUtils.setField(commercialSubject, "storedDocuments", results);
	    commercialSubject.removeDocument(DOCUMENT_NAME);
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
	public final void urlForName(){
		final CommercialSubject commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		ReflectionTestUtils.setField(commercialSubject, "id" , ID);
		Map<String,String> docs = new HashMap<>();
		docs.put(IMAGE, String.format(CommercialSubjectImpl.URL, ID, IMAGE ));
		ReflectionTestUtils.setField(commercialSubject, "storedDocuments", docs);
		
		Assert.assertEquals(String.format(CommercialSubjectImpl.URL, ID, IMAGE ), commercialSubject.urlForName(IMAGE));
	}
	
	@Test
	public final void urlForNameNotFound(){
		final CommercialSubject commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
		Assert.assertNull(commercialSubject.urlForName(IMAGE));
		
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
	
	@Test
	public final void assignWebLinkUrl() {
		final CommercialSubject opportunity = EntityUtil.create(CommercialSubjectImpl.class);
		opportunity.assignWebLink(WEB_LINK);
		Assert.assertEquals(String.format(OpportunityImpl.WWW_URL, WEB_LINK), opportunity.urlForName(WEB_LINK));
		Assert.assertTrue(opportunity.urlForName("kylie.com").startsWith("http://"));
		
	}
	
}

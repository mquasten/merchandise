package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.PersonConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class CommercialSubjectIntegrationTest {
	
	
	private static final String WEB_LINK = "kylie.com";
	
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	private Customer customer = PersonConstants.customer() ; 
	
	@Before
    public final void setup() {
    	customer=entityManager.merge(customer);
    	
    	waste.add(customer);
    	waste.add(customer.person());
    	entityManager.flush();
    }
	
	@After()
    public final void clean() {
		for(final BasicEntity entity : waste){
			entityManager.remove(entity);			
		}
		
		
	}
	
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void persist() {
		
	    CommercialSubject commercialSubject = new CommercialSubjectImpl(customer, "NAME" , "DESCRIPTION");
	    commercialSubject.assignWebLink(WEB_LINK);
	    commercialSubject = entityManager.merge(commercialSubject);
	    waste.add(commercialSubject);
	    entityManager.flush();
	    
	    CommercialSubject result = entityManager.find(CommercialSubjectImpl.class, commercialSubject.id());
	    Assert.assertEquals(commercialSubject, result);
	    Assert.assertEquals(1, result.documents().size());
	    Assert.assertEquals(customer, result.customer());
	    Assert.assertEquals(WEB_LINK, result.documents().keySet().iterator().next());
	    Assert.assertEquals(String.format(CommercialSubjectImpl.WWW_URL, WEB_LINK) ,new String(result.documents().values().iterator().next()));
	  
	 
	    entityManager.refresh(customer);
	    Assert.assertEquals(1, entityManager.find(CustomerImpl.class, customer.id()).commercialSubjects().size());
	    Assert.assertEquals(commercialSubject, entityManager.find(CustomerImpl.class, customer.id()).commercialSubjects().iterator().next());
	   
	}

}

package de.mq.merchandise.customer.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ProcuctClassification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class ClassificationIntergationTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public final void products() {
		final TypedQuery<ProcuctClassification> query = entityManager.createQuery("select p from ProductClassification p" ,ProcuctClassification.class);
		int counter=0;
		for(final ProcuctClassification procuctClassification : query.getResultList()) {
			Assert.assertTrue(procuctClassification.id().startsWith("P-"));
			if(procuctClassification.id().length()>3){
				Assert.assertNotNull(procuctClassification.parent().id());
			} else {
				Assert.assertNull(procuctClassification.parent());
			}
			counter++;
		}
		Assert.assertEquals(81, counter);
	}
	
	@Test
	public final void activities() {
		final TypedQuery<ActivityClassification> query = entityManager.createQuery("select a from ActivityClassification a" ,ActivityClassification.class);
		int counter=0;
		for(final ActivityClassification activityClassification : query.getResultList()) {
			Assert.assertTrue(activityClassification.id().startsWith("A-"));
			if(activityClassification.id().length()>3){
				Assert.assertNotNull(activityClassification.parent().id());
			} else {
				Assert.assertNull(activityClassification.parent());
			}
			counter++;
		}
		Assert.assertEquals(109, counter);
	}

}

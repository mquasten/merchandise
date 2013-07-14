package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class ClassificationIntegrationTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public final void activities() {
		Assert.assertEquals(109L, entityManager.createQuery(countString(ClassificationRepository.FIND_ALL_ACTIFITY_CLASSIFICATIONS), Number.class).getSingleResult().longValue());
	}
	
	@Test
	public final void products() {
		Assert.assertEquals(81L, entityManager.createQuery(countString(ClassificationRepository.FIND_ALL_PRODUCT_CLASSIFICATIONS), Number.class).getSingleResult().longValue());
	}

	private String countString(final String queryName) {
		return QueryUtils.createCountQueryFor(entityManager.createNamedQuery(queryName).unwrap(org.hibernate.Query.class).getQueryString().replaceFirst("[Oo][Rr][Dd][Ee][Rr].*[bB][Yy].*$", ""));
	}

}

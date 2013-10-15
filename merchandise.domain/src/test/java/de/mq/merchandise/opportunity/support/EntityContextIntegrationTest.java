package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class EntityContextIntegrationTest {
	
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	@After
    public final void clean() {
    	for(final BasicEntity entity : waste){
    		BasicEntity find = entityManager.find(entity.getClass(), entity.id());
    		System.out.println(find);
    		if( find != null)
			entityManager.remove(find);
    		entityManager.flush();
    	}
    	
    	
    }
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void create() {
		for(long i=1; i <= 10; i++){
			final EntityContext entityContext = new EntityContextImpl(i, Resource.Opportunity);
			waste.add(entityManager.merge(entityContext));
		}
		final TypedQuery<EntityContext> query = entityManager.createNamedQuery(EntityContextRepository.ENTITYCONTEXT_FOR_RESOURCE, EntityContext.class);
	    query.setParameter(EntityContextRepository.PARAMETER_RESOURCE, Resource.Opportunity);
	    final List<EntityContext> results = query.getResultList();
	    Assert.assertEquals(10, results.size());
	    Long counter=1L;
	    for(final EntityContext result : results){
	    	Assert.assertEquals(counter, result.reourceId());
	    	Assert.assertTrue(result.hasId());
	    	Assert.assertEquals(Resource.Opportunity, result.resource());
	    	Assert.assertNotNull(result.created());
	    	Assert.assertTrue(Math.abs(new Date().getTime() - result.created().getTime()) < 1000);
	    	counter++;
	    }
	    System.out.println(results.size());
	}
	
	

}

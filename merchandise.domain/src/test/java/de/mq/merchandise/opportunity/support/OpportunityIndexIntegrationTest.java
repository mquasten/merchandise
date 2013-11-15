package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
@Ignore
public class OpportunityIndexIntegrationTest {
	
	private static final double LONGITUDE = 44.5858333;
	private static final double LATITUDE = 48.8047222;
	
	private static final double LONGITUDE2= 43.046111;
	private static final double LATITUDE2 = 48.494444;
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	
	
	
	@After
    public final void clean() {
    	for(final BasicEntity entity : waste){
    		
    		final BasicEntity find = entityManager.find(entity.getClass(), entity.id());
    		
    		if( find != null)
			entityManager.remove(find);
    		entityManager.flush();
    	}
    	
    	
    }
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Rollback(false)
	public final void create() {
		
		final Customer customer  = entityManager.merge(PersonConstants.customer());
		final CommercialSubject commercialSubject = entityManager.merge(new CommercialSubjectImpl(customer, "Krupp'sche Geschäfte", "Ihr Prinzip und ihre Folgen für die Allgemeinheit"));
		final Opportunity opportunity = entityManager.merge(new OpportunityImpl(customer,"Wintergewitter" , "Gescheitertes Date von Friedrich und Hermann", Kind.Tender));
		
		final OpportunityIndexImpl opportunityIndex = entityManager.merge(new OpportunityIndexImpl(opportunity));
		waste.add(opportunityIndex);
		waste.add(opportunity);
		waste.add(commercialSubject);
		waste.add(customer);
		waste.add(customer.person());
		
		final String updateSql="update OpportunityIndex set geometry = ST_GeometryFromText(:geometry), searchVector = to_tsvector(coalesce(:name,'') || ' ' || coalesce(:description,'')) where id = :id";
		
		final Query query = entityManager.createQuery(updateSql);
		query.setParameter("geometry",  longitudeLatitudeToText(LONGITUDE, LATITUDE));
		query.setParameter("id", opportunity.id());
		query.setParameter("name", opportunity.name());
		query.setParameter("description", opportunity.description());
		Assert.assertEquals(1, query.executeUpdate());
		final OpportunityIndexImpl result = entityManager.find(OpportunityIndexImpl.class, opportunityIndex.id());
		
		Assert.assertEquals(opportunity, result.opportunity());
		Assert.assertEquals(opportunityIndex, result);
		Assert.assertEquals(result.id(), result.opportunity().id());
		
		final String searchSql="select i from OpportunityIndex i where st_distance(i.geometry, ST_GeometryFromText(:geometry) ) < :distance)";
		final TypedQuery<OpportunityIndex> typedQuery = entityManager.createQuery(searchSql, OpportunityIndex.class);
		typedQuery.setParameter("geometry", longitudeLatitudeToText(LONGITUDE2, LATITUDE2));
		typedQuery.setParameter("distance", 150 * 1e3);
		for(final OpportunityIndex  index : typedQuery.getResultList()) {
			if( index.opportunity().equals(opportunity) ) {
				Assert.assertEquals(opportunity.id(), index.id());
				return;
			}
		}
		Assert.fail("At least one row should be returned");
		
	}

	private String longitudeLatitudeToText(final double longitude, final double  latitude) {
		return String.format("Point(%s %s)", longitude, latitude);
	}

}

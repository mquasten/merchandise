package de.mq.merchandise.opportunity.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.ContactBuilderFactory;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class OpportunityIndexIntegrationTest {
	
	private static final double LONGITUDE2= 43.046111;
	private static final double LATITUDE2 = 48.494444;
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	private final List<String> indexWaste = new ArrayList<>();
	private ContactBuilderFactory contactBuilderFactory = new ContactBuilderFactoryImpl();
	
	private boolean isHSQL=false;;
	
	@Autowired
	private DataSource dataSource;
	
	private final Address address = contactBuilderFactory.addressBuilder().withCity("Stalingrad").withZipCode("99999").withHouseNumber("unkownn").withStreet("unkown").withCoordinates(contactBuilderFactory.coordinatesBuilder().withLongitude(44.5858333).withLatitude(48.8047222).build()).build();
	
	@Before
	public  void before() {
		isHSQL=false;
		
		try (final Connection con = dataSource.getConnection()) {
			
			if( con.getMetaData().getDriverName().startsWith("HSQL")){
				isHSQL=true;
				return;
			}
				
			
		} catch (final SQLException ex) {
			 System.err.println(ex.getMessage());
			
		}
		
	}
	
	
	@After
    public final void clean() {
		for(String id : indexWaste){
			final OpportunityIndex inDenStaub= entityManager.find(AbstractOpportunityIndex.class, id);
			if( inDenStaub == null){
				continue;
			}
			entityManager.remove(inDenStaub);
			entityManager.flush();
		}
		
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
		final Address cityAddress = entityManager.merge(address);
		
		final OpportunityIndex opportunityIndexFullTextSearch = entityManager.merge(new OpportunityFullTextSearchIndexImpl(opportunity));
		
		final OpportunityIndex opportunityGeoLocationIndex =  entityManager.merge(new OpportunityGeoLocationIndexImpl(opportunity, cityAddress));
		
		
		
		indexWaste.add(opportunityIndexFullTextSearch.id());
		indexWaste.add(opportunityGeoLocationIndex.id());
		
		waste.add(cityAddress);
		waste.add(opportunity);
		waste.add(commercialSubject);
		waste.add(customer);
		waste.add(customer.person());
		
		final String updateSqlTS="update OpportunityFullTextSearchIndex set  searchVector = to_tsvector(coalesce(:name,'') || ' ' || coalesce(:description,'')) where id = :id";
		
		final Query queryTS = entityManager.createQuery(updateSqlTS);

		queryTS.setParameter("id", opportunityIndexFullTextSearch.id());
		queryTS.setParameter("name", opportunity.name());
		queryTS.setParameter("description", opportunity.description());
		
		if( ! isHSQL) {
		   Assert.assertEquals(1, queryTS.executeUpdate());
		}
		findAndCheckOpportunityIndex(opportunity, opportunityIndexFullTextSearch);
		
		
		
		final String updateSqlGIS="update OpportunityGeoLocationIndex set  geometry = ST_GeometryFromText(:geometry) where id = :id";
		
		final Query queryGIS = entityManager.createQuery(updateSqlGIS);
		queryGIS.setParameter("geometry",  longitudeLatitudeToText(address.coordinates().longitude(), address.coordinates().latitude()));
		queryGIS.setParameter("id",opportunityGeoLocationIndex.id());
		if (! isHSQL){
			Assert.assertEquals(1, queryGIS.executeUpdate());
		}
		
		findAndCheckOpportunityIndex(opportunity, opportunityGeoLocationIndex);	
		
		
		if( isHSQL){
			return;
		}
		final String searchSql="select  distinct  o from Opportunity o where  exists(select 1 from OpportunityFullTextSearchIndex ts where checkvector(ts.searchVector, to_tsquery(:pattern))=true and o.id=ts.opportunity.id) and exists(select 1 from OpportunityGeoLocationIndex gis  where  st_distance(gis.geometry, ST_GeometryFromText(:geometry) ) < :distance  and o.id = gis.opportunity.id )";
		System.out.println(searchSql);
		final TypedQuery<Opportunity> typedQuery = entityManager.createQuery(searchSql, Opportunity.class);
		typedQuery.setParameter("geometry", longitudeLatitudeToText(LONGITUDE2, LATITUDE2));
		typedQuery.setParameter("distance", 150 * 1e3);
		typedQuery.setParameter("pattern", "HERMANN&FRIEDRICH&DATE&WINTERGEWITTER");
		for(final Opportunity  res : typedQuery.getResultList()) {
			if( res.equals(opportunity) ) {
				System.out.println(res.name());
				Assert.assertEquals(opportunity.id(), res.id());
				return;
			}
		}
		Assert.fail("At least one row should be returned"); 
	

	}

	private void findAndCheckOpportunityIndex(final Opportunity opportunity, final OpportunityIndex opportunityIndex) {
		final OpportunityIndex result = entityManager.find(AbstractOpportunityIndex.class, opportunityIndex.id());
		
		Assert.assertEquals(opportunity, result.opportunity());
		Assert.assertEquals(opportunityIndex, result);
		Assert.assertEquals(opportunity.id(), result.opportunity().id());
		Assert.assertEquals(opportunityIndex.id(), result.id());
	}

	private String longitudeLatitudeToText(final double longitude, final double  latitude) {
		return String.format("Point(%s %s)", longitude, latitude);
	}
	
	

}

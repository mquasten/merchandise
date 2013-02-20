package de.mq.merchandise.contact.support;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.support.GeoLocationImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class GeoLocationIntegrationTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public final void search() {
		final TypedQuery<CityAddress> query = entityManager.createNamedQuery(GeoLocationImpl.QUERY_CITY_BY_NAME_PATTERN,CityAddress.class);
		query.setParameter("city", "Weg%");
		query.setParameter("country", "DE");
		final List<CityAddress> results = query.getResultList();
		Assert.assertTrue(results.size()> 0);
		for(final CityAddress address : results){
			if( address.city().equals("Wegberg")){
				System.out.println(address.city() + " " + address.zipCode());
				return;
			}
		}
		Assert.fail("Searched city not found");
	}

}

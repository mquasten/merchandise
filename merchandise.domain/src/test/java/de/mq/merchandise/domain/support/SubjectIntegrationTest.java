package de.mq.merchandise.domain.support;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.subject.support.SubjectImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class SubjectIntegrationTest {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private DataSource dataSource;
	
	private final List<Entry<Long,Class<?>>> waste = new ArrayList<>();
	
	@Before
	@After
	@Transactional()
	@Rollback(false)
	public final void cleanup() throws SQLException {
		waste.forEach( e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
		waste.clear();

	    
	}
	
	@AfterClass
	public static final void finish() {
		System.out.println("all beauty...");
		
	}
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void create() {
		
		SubjectImpl subject = new SubjectImpl("Dolls for you");
		entityManager.persist(subject);
		
		Assert.assertTrue(subject.id().isPresent());
		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(),subject.getClass()));
		entityManager.flush();
	}

}

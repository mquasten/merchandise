package de.mq.merchandise.reference.support;

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

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceImpl;
import de.mq.merchandise.reference.support.ReferenceKeyImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})

public class ReferenceIntegrationTest {
	
	@PersistenceContext()
	private EntityManager entityManager;
	
	private List<Reference> waste = new ArrayList<>();
	
	@Before
	@After
	public final void cleanup() {
		for(Reference inDenStaub: waste){
			entityManager.remove(inDenStaub);
		}
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public final void reference() {
		final Reference reference = new ReferenceImpl("XX", Kind.Language);
		
		waste.add(entityManager.merge(reference));
		Assert.assertEquals(reference,  entityManager.find(ReferenceImpl.class, new ReferenceKeyImpl("XX", Kind.Language)));
	}

}

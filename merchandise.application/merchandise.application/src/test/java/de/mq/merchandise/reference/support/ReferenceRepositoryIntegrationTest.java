package de.mq.merchandise.reference.support;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/repositories.xml"})
@Ignore
public class ReferenceRepositoryIntegrationTest {
	
	@Autowired
	private ReferenceRepository referenceRepository;
	
	@Test
	@Transactional
	public final void languages() {
		
		final Set<String> results = new HashSet<>();
		for(Reference reference : referenceRepository.forType(Kind.Language)) {
			results.add(reference.key());
		}
		Assert.assertTrue(results.contains("DE"));
		Assert.assertTrue(results.contains("EN"));
		
		
	}
	
	

}

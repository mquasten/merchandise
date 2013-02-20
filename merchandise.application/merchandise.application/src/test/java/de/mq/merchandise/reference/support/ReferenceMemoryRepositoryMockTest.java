package de.mq.merchandise.reference.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;

public class ReferenceMemoryRepositoryMockTest {
	
	final ReferenceRepository referenceRepository = new ReferenceMemoryRepositoryMock();
	
	@Test
	public final void country() {
		
		final List<Reference> results = referenceRepository.forType(Kind.Country);
		Assert.assertEquals(3, results.size());
		final Set<String> keys = checkDupplicates(results, Kind.Country);
		Assert.assertTrue(keys.contains("DE"));
		Assert.assertTrue(keys.contains("GB"));
		Assert.assertTrue(keys.contains("US"));
	}
	
	@Test
	public final void language() {
		final List<Reference> results = referenceRepository.forType(Kind.Language);
		Assert.assertEquals(2, results.size());
		final Set<String> keys = checkDupplicates(results, Kind.Language);
		
		Assert.assertTrue(keys.contains("de"));
		Assert.assertTrue(keys.contains("en"));
	}

	private Set<String> checkDupplicates(final List<Reference> results, Kind kind) {
		final Set<String> keys = new HashSet<>();
		for(final Reference reference : results){
			Assert.assertEquals(kind, reference.referenceType());
			Assert.assertFalse(keys.contains(reference.key()));
			keys.add(reference.key());
		}
		return keys;
	}
	

}

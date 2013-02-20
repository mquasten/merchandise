package de.mq.merchandise.reference.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceKeyImpl;
import de.mq.merchandise.util.EntityUtil;

public class ReferenceKeyTest {
	
	static final String LANGUAGE = "DE";

	@Test
	public final void createReferenceKey() {
		final ReferenceKeyImpl referenceKey = new ReferenceKeyImpl(LANGUAGE, Kind.Language);
		Assert.assertEquals(LANGUAGE, referenceKey.key());
		Assert.assertEquals( Kind.Language, referenceKey.referenceType());
		
	}
	
	@Test
	public final void createInvalid() {
		final ReferenceKeyImpl referenceKey= EntityUtil.create(ReferenceKeyImpl.class);
		Assert.assertNull(referenceKey.key());
		Assert.assertNull(referenceKey.referenceType());
	}
	
	@Test
	public final void hash() {
		Assert.assertEquals(LANGUAGE.hashCode() + Kind.Language.hashCode() , new ReferenceKeyImpl(LANGUAGE, Kind.Language).hashCode());
	}
	
	@Test
	public final void equals() {
		Assert.assertTrue(new ReferenceKeyImpl(LANGUAGE, Kind.Language).equals(new ReferenceKeyImpl(LANGUAGE, Kind.Language)));
		final ReferenceKeyImpl referenceKey= EntityUtil.create(ReferenceKeyImpl.class);
		Assert.assertTrue(referenceKey.equals(referenceKey));
	}

}

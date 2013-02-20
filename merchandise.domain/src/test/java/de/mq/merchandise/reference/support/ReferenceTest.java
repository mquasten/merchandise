package de.mq.merchandise.reference.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceImpl;
import de.mq.merchandise.util.EntityUtil;

public class ReferenceTest {
	
	@Test
	public final void createReference() {
		final Reference reference = new ReferenceImpl(ReferenceKeyTest.LANGUAGE, Kind.Language);
		Assert.assertEquals(ReferenceKeyTest.LANGUAGE, reference.key());
		Assert.assertEquals(Kind.Language, reference.referenceType());
	}
	
	@Test
	public final void createInvalidReference() {
		final Reference reference = EntityUtil.create(ReferenceImpl.class);
		Assert.assertNull(reference.key());
		Assert.assertNull(reference.referenceType());
	}
	
	@Test
	public final void hash() {
		Assert.assertEquals(ReferenceKeyTest.LANGUAGE.hashCode() + Kind.Language.hashCode(), new ReferenceImpl(ReferenceKeyTest.LANGUAGE, Kind.Language).hashCode());
	}
	
	@Test
	public final void equals(){
		Assert.assertTrue(new ReferenceImpl(ReferenceKeyTest.LANGUAGE, Kind.Language).equals(new ReferenceImpl(ReferenceKeyTest.LANGUAGE, Kind.Language)));
		final Reference reference = EntityUtil.create(ReferenceImpl.class);
		Assert.assertTrue(reference.equals(reference));
	}
	
	@Test
	public final void kind() {
		for(final Reference.Kind kind : Reference.Kind.values()){
			Assert.assertEquals(kind, Reference.Kind.valueOf(kind.name()));
		}
	}

}

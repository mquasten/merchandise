package de.mq.merchandise.opportunity.support;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.EntityUtil;

public class EntityContextTest {
	
	private static final Long ID = 196828L;

	@Test
	public final void constructorUpdate() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Opportunity);
		Assert.assertEquals(ID,  entityContext.reourceId());
		Assert.assertEquals(Resource.Opportunity, entityContext.resource());
		Assert.assertTrue(Math.abs(new Date().getTime() - entityContext.created().getTime()) < 1000);
		Assert.assertFalse(entityContext.isForDeleteRow());
		Assert.assertFalse(entityContext.hasId());
	}
	
	
	@Test
	public final void constructorDelete() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject, true);
		Assert.assertEquals(ID,  entityContext.reourceId());
		Assert.assertEquals(Resource.Subject, entityContext.resource());
		Assert.assertTrue(Math.abs(new Date().getTime() - entityContext.created().getTime()) < 1000);
		Assert.assertTrue(entityContext.isForDeleteRow());
		Assert.assertFalse(entityContext.hasId());
	}
	
	@Test
	public final void id() {
		final EntityContext entityContext = EntityUtil.create(EntityContextImpl.class);
		Assert.assertFalse(entityContext.hasId());
		ReflectionTestUtils.setField(entityContext, "id", ID);
		Assert.assertTrue(entityContext.hasId());
		Assert.assertEquals(ID,(Long)  entityContext.id());
	}
	
	@Test
	public final void equals() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject, true);
		Assert.assertTrue(entityContext.equals(new EntityContextImpl(ID, Resource.Subject, true)));
		Assert.assertFalse(entityContext.equals(new EntityContextImpl(ID, Resource.Subject, false)));
		Assert.assertFalse(entityContext.equals(new EntityContextImpl(ID, Resource.Opportunity, true)));
		Assert.assertFalse(entityContext.equals(new EntityContextImpl(ID+1, Resource.Subject, false)));
	}
	
	@Test
	public final void hashCodeCheck() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject, true);
		Assert.assertEquals(entityContext.resource().hashCode() + entityContext.reourceId().hashCode()+ Boolean.TRUE.hashCode() + entityContext.created().hashCode(), entityContext.hashCode());
	}

}

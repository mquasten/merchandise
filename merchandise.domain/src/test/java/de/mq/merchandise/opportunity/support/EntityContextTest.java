package de.mq.merchandise.opportunity.support;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.EntityUtil;

public class EntityContextTest {
	
	private static final int COUNT = 4711;
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
	
	@Test
	public final void assign() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject, true);
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		entityContext.assign(Opportunity.class, opportunity);
		
		
		final Map<Class<? extends Object>, Object> results =  getReferenceMap(entityContext);
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(Opportunity.class, results.keySet().iterator().next());
		Assert.assertEquals(opportunity, results.values().iterator().next());
		
		
	}


	
	@SuppressWarnings("unchecked")
	private Map<Class<? extends Object>, Object> getReferenceMap(final EntityContext entityContext) {
		return (Map<Class<? extends Object>, Object>) ReflectionTestUtils.getField(entityContext, "references");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void assignWrongType() {
		ReflectionTestUtils.invokeMethod(EntityUtil.create(EntityContextImpl.class), "assign", Opportunity.class , EntityUtil.create(CommercialSubjectImpl.class) );
	}
	
	@Test
	public final void reference() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject, true);
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		final Map<Class<? extends Object>,Object> results =  getReferenceMap(entityContext);
		results.put(Opportunity.class, opportunity);
		
		Assert.assertEquals(opportunity, entityContext.reference(Opportunity.class));
	}
	
	@Test(expected=IllegalStateException.class)
	public final void referenceNotFound() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		entityContext.reference(Opportunity.class);
	}
	
	@Test
	public final void containsReference() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		Assert.assertFalse(entityContext.containsReference(Opportunity.class));
		
		final Map<Class<? extends Object>,Object> results =  getReferenceMap(entityContext);
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		results.put(Opportunity.class, opportunity);
		
		Assert.assertTrue(entityContext.containsReference(Opportunity.class));
	}
	
	@Test
	public final void assignState() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		Assert.assertNull( ReflectionTestUtils.getField(entityContext, "lastErrorDate"));
		Assert.assertEquals(0, ReflectionTestUtils.getField(entityContext, "errorCounter"));
		entityContext.assign(State.Conflict);
		
		
		final Date lastErrorDate = (Date) ReflectionTestUtils.getField(entityContext, "lastErrorDate");
		Assert.assertTrue(Math.abs(lastErrorDate.getTime() - new Date().getTime() ) < 50);
		Assert.assertEquals(1, ReflectionTestUtils.getField(entityContext, "errorCounter"));
	}
	
	@Test
	public final void assignStateNoError() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		entityContext.assign(State.Skipped);
		Assert.assertNull( ReflectionTestUtils.getField(entityContext, "lastErrorDate"));
		Assert.assertEquals(0, ReflectionTestUtils.getField(entityContext, "errorCounter"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void assignNotAllowed() {
		new EntityContextImpl(ID, Resource.Subject).assign(State.New);
	}
	
	@Test
	public final void finised() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		for(final State state : State.values()){
			if( ! state.assignable()){
				continue;
			}
			entityContext.assign(state);
			Assert.assertEquals(state.finised(), entityContext.finished());
		}
	}

	@Test
	public final void staes() {
		int count=0;
		for(final State state : State.values()){
			Assert.assertEquals(state, State.valueOf(state.name()));
			count++;
		}
		
		Assert.assertEquals(7, count);
		
		Assert.assertFalse(State.New.assignable());
		Assert.assertFalse(State.New.error());
		Assert.assertFalse(State.New.finised());
		
		
		Assert.assertTrue(State.Conflict.assignable());
		Assert.assertTrue(State.Conflict.error());
		Assert.assertFalse(State.Conflict.finised());
		
		
		Assert.assertTrue(State.Unauthorized.assignable());
		Assert.assertTrue(State.Unauthorized.error());
		Assert.assertFalse(State.Unauthorized.finised());
		
		
		Assert.assertTrue(State.Forbidden.assignable());
		Assert.assertTrue(State.Forbidden.error());
		Assert.assertFalse(State.Forbidden.finised());
		
		Assert.assertTrue(State.Unkown.assignable());
		Assert.assertTrue(State.Unkown.error());
		Assert.assertFalse(State.Unkown.finised());
		
		Assert.assertTrue(State.Ok.assignable());
		Assert.assertFalse(State.Ok.error());
		Assert.assertTrue(State.Ok.finised());
		
		Assert.assertTrue(State.Skipped.assignable());
		Assert.assertFalse(State.Skipped.error());
		Assert.assertTrue(State.Skipped.finised());

		
	}
	
	@Test
	public final void entityAggregation() {
		final Date date = new GregorianCalendar(1968, 04, 28).getTime();
		final EntityContextAggregation entityContextAggregation = new EntityContextAggregationImpl(COUNT, date);
		Assert.assertEquals(date, entityContextAggregation.minDate());
		Assert.assertEquals(COUNT, entityContextAggregation.counter());
	}
	
	@Test
	public final void entityAggregationDefaults(){
		final EntityContextAggregation entityContextAggregation = new EntityContextAggregationImpl(null, null);
		Assert.assertEquals(0, entityContextAggregation.counter());
		Assert.assertTrue((new Date().getTime()  -entityContextAggregation.minDate().getTime()) < 5 );
	}
	
	@Test
	public final void error() {
		final EntityContext entityContext = new EntityContextImpl(ID, Resource.Subject);
		for(final State state : State.values()){
			if(! state.assignable()){
				continue;
			}
		
			entityContext.assign(state);
			Assert.assertEquals(state.error(), entityContext.error());
		}
		
	}
}

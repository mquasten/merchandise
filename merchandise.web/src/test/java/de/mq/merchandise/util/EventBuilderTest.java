package de.mq.merchandise.util;



import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;

public class EventBuilderTest {
	
	private static final Long EVENT_ID = 19680528L;

	@Test
	public final void createVoid() {
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		final Event<Long,Void> event = EventBuilder.of(EVENT_ID).withParameter(subject).build();
		Assert.assertEquals(EVENT_ID,  event.id());
		final Map<Class<?>, ?> params = event.parameter();
		Assert.assertEquals(2, params.size());
		Assert.assertEquals(subject, params.get(Subject.class));
		Assert.assertEquals(subject, params.get(subject.getClass()));
		
		Assert.assertEquals(EVENT_ID.hashCode(), event.hashCode());
		Assert.assertTrue(event.equals((Event<Long,Void>)() -> EVENT_ID));
		Assert.assertFalse(event.equals( EVENT_ID));
		Assert.assertEquals(EVENT_ID.toString(), event.toString());
		
		
	}

}

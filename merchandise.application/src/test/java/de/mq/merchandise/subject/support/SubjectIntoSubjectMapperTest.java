package de.mq.merchandise.subject.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class SubjectIntoSubjectMapperTest {
	

	private static final String DESC = "Pets4You";
	private static final String NAME = "PetStore";
	private final Mapper<Subject,Subject> mapper = new  SubjectIntoSubjectMapperImpl();
	
	@Test
	public final void mapInto() {
		final Subject source = Mockito.mock(Subject.class);
		Mockito.when(source.name()).thenReturn(NAME);
		Mockito.when(source.description()).thenReturn(DESC);
		
		final Subject target = BeanUtils.instantiateClass(SubjectImpl.class);
		Assert.assertNull(target.name());
		Assert.assertNull(target.description());
		
		mapper.mapInto(source, target);
		
		Assert.assertEquals(NAME, target.name());
		Assert.assertEquals(DESC, target.description());
	}
	

}

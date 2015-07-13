package de.mq.merchandise.subject.support;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class ConditionIntoSubjectMapperTest {
	

	private static final long ID = 19680528L;
	private static final String CONDITION_TYPE_QUALITY = "Quality";
	private final Mapper<Condition, Subject>  mapper = new ConditionIntoSubjectMapperImpl();
	
	@Test
	public final void mapInto() {
		
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.id()).thenReturn(Optional.of(ID));
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_TYPE_QUALITY);
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.String);
		
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
	
		subject.add(CONDITION_TYPE_QUALITY, ConditionDataType.IntegralNumber);
		
		mapper.mapInto(condition, subject);
		
		final Optional<Condition> result = subject.conditions().stream().findFirst();
		Assert.assertTrue(result.isPresent());
		Assert.assertEquals(CONDITION_TYPE_QUALITY, result.get().conditionType());
		
		Assert.assertEquals(ConditionDataType.String, result.get().conditionDataType());
		
	}
	
	@Test
	public final void mapIntoNew() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.id()).thenReturn(Optional.empty());
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_TYPE_QUALITY);
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.String);
		
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		Assert.assertTrue(subject.conditions().isEmpty());
		mapper.mapInto(condition, subject);
		
		Assert.assertEquals(1, subject.conditions().size());
		Assert.assertTrue(subject.conditions().stream().findFirst().isPresent());
		Assert.assertEquals(CONDITION_TYPE_QUALITY, subject.conditions().stream().findFirst().get().conditionType());
		Assert.assertEquals(ConditionDataType.String, subject.conditions().stream().findFirst().get().conditionDataType());
		Assert.assertTrue(subject.conditions().stream().findFirst().isPresent());
		Assert.assertEquals(Optional.empty(), subject.conditions().stream().findFirst().get().id());
		
	}

}

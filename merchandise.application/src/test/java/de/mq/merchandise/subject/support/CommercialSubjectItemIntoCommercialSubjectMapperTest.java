package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import javax.persistence.Id;
import javax.persistence.OneToMany;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class CommercialSubjectItemIntoCommercialSubjectMapperTest {

	private static final String ITEM_NAME = "Kylie";
	private static final long ID = 19680528L;
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);
	private final Mapper<CommercialSubjectItem,CommercialSubject> mapper = new CommercialSubjectItemIntoCommercialSubjectMapperImpl(subjectService);
	private final CommercialSubjectItem source = Mockito.mock(CommercialSubjectItem.class);
	private final Subject subject = Mockito.mock(Subject.class);
	@Before
	public final void setup() {
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		Mockito.when(source.subject()).thenReturn(subject);
		Mockito.when(source.name()).thenReturn(ITEM_NAME);
		Mockito.when(source.mandatory()).thenReturn(true);
	}
	
	@Test
	public final void mapIntoNew() {
		Mockito.when(source.id()).thenReturn(Optional.empty());
		
		CommercialSubject target = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		
		mapper.mapInto(source, target);
		Assert.assertEquals(1, target.commercialSubjectItems().size());
		Assert.assertTrue(target.commercialSubjectItems().stream().findAny().isPresent());
		Assert.assertEquals(ITEM_NAME, target.commercialSubjectItems().stream().findFirst().get().name());
		Assert.assertTrue(target.commercialSubjectItems().stream().findFirst().get().mandatory());
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void mapIntoUpdate() {
		Mockito.when(source.id()).thenReturn(Optional.of(ID));
	
		final CommercialSubjectItem toBeUpdated = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class);
	
		
		ReflectionUtils.doWithFields(toBeUpdated.getClass(), field-> ReflectionTestUtils.setField(toBeUpdated, field.getName(), ID), field -> field.isAnnotationPresent(Id.class));
		
		ReflectionUtils.doWithFields(toBeUpdated.getClass(), field -> ReflectionTestUtils.setField(toBeUpdated, field.getName(), subject) , field ->  field.getType().equals(Subject.class));
		final CommercialSubject target = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		ReflectionUtils.doWithFields(target.getClass(), field -> ((Collection<CommercialSubjectItem>)ReflectionTestUtils.getField(target, field.getName())).add(toBeUpdated),field -> field.isAnnotationPresent(OneToMany.class)&&field.getAnnotation(OneToMany.class).targetEntity().equals(CommercialSubjectItemImpl.class));
		
	
		mapper.mapInto(source, target);
		
		Assert.assertEquals(1, target.commercialSubjectItems().size());
		Assert.assertTrue(target.commercialSubjectItems().stream().findAny().isPresent());
		
		Assert.assertEquals(ITEM_NAME, target.commercialSubjectItems().stream().findFirst().get().name());
		Assert.assertTrue(target.commercialSubjectItems().stream().findFirst().get().mandatory());
		Assert.assertEquals(subject, target.commercialSubjectItems().stream().findFirst().get().subject());
		
	}
}

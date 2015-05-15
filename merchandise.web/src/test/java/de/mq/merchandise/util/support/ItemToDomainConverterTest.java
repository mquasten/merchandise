package de.mq.merchandise.util.support;



import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;
import de.mq.merchandise.subject.support.TestConstants;

public class ItemToDomainConverterTest {
	
	
	
	private final Converter<Item, Subject> converter = new ItemToDomainConverterImpl<Subject>(SubjectImpl.class, TestConstants.SUBJECT_COLS_CLASS);

	@Test
	public final void convert() {
		final Subject subject = converter.convert(TestConstants.itemMock());
		
		Assert.assertEquals(TestConstants.SUBJECT_DESCRIPTION, subject.description());
		Assert.assertEquals(TestConstants.SUBJECT_NAME, subject.name());
		Assert.assertEquals(Optional.of(TestConstants.ID), subject.id());
		Assert.assertTrue(new Date().getTime() - ((Date)ReflectionTestUtils.getField(subject, "dateCreated")).getTime() <  100);
	}
	
}

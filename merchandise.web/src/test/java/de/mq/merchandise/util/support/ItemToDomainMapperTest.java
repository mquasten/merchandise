package de.mq.merchandise.util.support;

import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;
import de.mq.merchandise.subject.support.TestConstants;

public class ItemToDomainMapperTest {

	public static final String SUBJECT_DESCRIPTION = "Pets for you";
	public static final String SUBJECT_NAME = "PetStore";
	public static final Long ID = 19680528L;
	final Item item = new PropertysetItem();

	private final Converter<Item, Subject> converter = new ItemToDomainConverterImpl<Subject>(SubjectImpl.class, TestConstants.SUBJECT_COLS_CLASS);

	@Test
	public final void convert() {

		item.addItemProperty(TestConstants.SUBJECT_COLS_ID, new ObjectProperty<>(ID));
		item.addItemProperty(TestConstants.SUBJECT_COLS_NAME, new ObjectProperty<>(SUBJECT_NAME));
		item.addItemProperty(TestConstants.SUBJECT_COLS_DESC, new ObjectProperty<>(SUBJECT_DESCRIPTION));

		final Subject subject = converter.convert(item);

		Assert.assertEquals(SUBJECT_DESCRIPTION, subject.description());
		Assert.assertEquals(SUBJECT_NAME, subject.name());
		Assert.assertEquals(Optional.of(ID), subject.id());
		Assert.assertTrue(new Date().getTime() - ((Date) ReflectionTestUtils.getField(subject, "dateCreated")).getTime() < 200);
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(converter.convert(null));
	}

}

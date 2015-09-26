package de.mq.merchandise.util.support;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.customer.support.CustomerImpl;
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
	public final void convertOnlyName() {
		
		item.addItemProperty(TestConstants.SUBJECT_COLS_ID, new ObjectProperty<>(ID));
		item.addItemProperty(TestConstants.SUBJECT_COLS_NAME, new ObjectProperty<>(SUBJECT_NAME));
		item.addItemProperty(TestConstants.SUBJECT_COLS_DESC, new ObjectProperty<>(SUBJECT_DESCRIPTION));
		
		
		@SuppressWarnings("unchecked")
		final Converter<Item, Subject> converter = new ItemToDomainConverterImpl<Subject>(SubjectImpl.class, new Enum[] {((Collection<Enum<?>>)(Collection<?>) TestConstants.SUBJECT_COLS).stream().filter(col -> col.name().toLowerCase().equals("name")).findFirst().get()});
		final Subject result = converter.convert(item);
		Assert.assertEquals(SUBJECT_NAME, result.name());
		Assert.assertNull(result.description());
		Assert.assertFalse(result.id().isPresent());
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(converter.convert(null));
	}
	
	@Test
	public final void withChild() {
	
		
		item.addItemProperty(SubjectColsTest.Customer, new ObjectProperty<>(ID));
		item.addItemProperty(SubjectColsTest.Name, new ObjectProperty<>(SUBJECT_NAME));
		
		final Converter<Item, Subject> converter = new ItemToDomainConverterImpl<Subject>(SubjectImpl.class, SubjectColsTest.class).withChild(SubjectColsTest.Customer, CustomerImpl.class);
		final Subject result = converter.convert(item);
		Assert.assertTrue(result.customer().id().isPresent());
		Assert.assertEquals(ID, result.customer().id().get());
		Assert.assertNull(result.description());
		Assert.assertFalse(result.id().isPresent());
		Assert.assertEquals(SUBJECT_NAME, result.name());
	}

	enum SubjectColsTest {
		Customer,
		Name
	}
}



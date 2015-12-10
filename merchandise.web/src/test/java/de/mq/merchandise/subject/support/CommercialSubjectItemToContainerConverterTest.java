package de.mq.merchandise.subject.support;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectItemToContainerConverterTest {
	private static final long SUBJECT_ID = 19680528L;

	private static final String ITEM_NAME = "itemName";

	private static final long COMMERCIAL_SUBJECT_ITEM_ID = 4711L;

	private final CommercialSubject commercialSubjet = Mockito.mock(CommercialSubject.class);

	private CommercialSubjectItem commercialSubjectItem;
	private Converter<Collection<CommercialSubjectItem>, Container> converter = new CommercialSubjectItemToContainerConverter();
	final Subject subject = new SubjectImpl(Mockito.mock(Customer.class), "subjectName");

	private final Collection<CommercialSubjectItem> source = new ArrayList<>();

	@Before
	public final void setup() {
		setId(subject, SUBJECT_ID);
		commercialSubjectItem = new CommercialSubjectItemImpl(ITEM_NAME, commercialSubjet, subject, true);
		setId(commercialSubjectItem, COMMERCIAL_SUBJECT_ITEM_ID);
		source.add(commercialSubjectItem);
	}

	private void setId(final Object entity, final Object value) {
		ReflectionUtils.doWithFields(entity.getClass(), field -> ReflectionTestUtils.setField(entity, field.getName(), value), field -> field.isAnnotationPresent(Id.class));
	}

	@Test
	public final void convert() {
		final Container result = converter.convert(source);
		Assert.assertEquals(1, result.getItemIds().size());
		Assert.assertTrue(result.getItemIds().stream().findAny().isPresent());
		final Item item = result.getItem(result.getItemIds().stream().findAny().get());
		Assert.assertEquals(COMMERCIAL_SUBJECT_ITEM_ID, item.getItemProperty(CommercialSubjectItemCols.Id).getValue());
		Assert.assertEquals(ITEM_NAME, item.getItemProperty(CommercialSubjectItemCols.Name).getValue());
		Assert.assertEquals(SUBJECT_ID, item.getItemProperty(CommercialSubjectItemCols.Subject).getValue());
		Assert.assertTrue((Boolean) item.getItemProperty(CommercialSubjectItemCols.Mandatory).getValue());
	}

	@Test
	public final void convertDefaults() {
		ReflectionUtils.doWithFields(commercialSubjectItem.getClass(), field -> ReflectionTestUtils.setField(commercialSubjectItem, field.getName(), null), field -> !field.getType().isPrimitive() && !field.getType().equals(Subject.class) && ! Modifier.isStatic(field.getModifiers()));
		final Container result = converter.convert(source);
		Assert.assertEquals(1, result.getItemIds().size());
		Assert.assertTrue(result.getItemIds().stream().findAny().isPresent());
		final Item item = result.getItem(result.getItemIds().stream().findAny().get());
		Arrays.asList(CommercialSubjectItemCols.Name, CommercialSubjectItemCols.Id).stream().forEach(col -> Assert.assertEquals(col.nvl(), item.getItemProperty(col).getValue()));
		Assert.assertEquals(SUBJECT_ID, item.getItemProperty(CommercialSubjectItemCols.Subject).getValue());
		Assert.assertTrue((Boolean) item.getItemProperty(CommercialSubjectItemCols.Mandatory).getValue());

	}
}

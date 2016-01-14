package de.mq.merchandise.util.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;
import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.TableContainerColumns;

public class DomainToItemConverterTest {

	private static final String CUSTOMER_FIELD = "customer";
	private static final String EMPTY_STRING = "";
	private static final String ID_FIELD = "id";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String NAME_FIELD = "name";
	private static final String SUBJECT_DESC = "subjectDesc";
	private static final String SUBJECT_NAME = "subjectName";
	private static final Long ID = 19680528L;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	final Converter<Subject, Item> converter = new DomainToItemConverterImpl(TestConstants.SUBJECT_COLS_CLASS);

	@Test
	public final void toWeb() {
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		ReflectionTestUtils.setField(subject, DESCRIPTION_FIELD, SUBJECT_DESC);
		ReflectionTestUtils.setField(subject, ID_FIELD, ID);
		Assert.assertNotNull(subject.created());

		final Item item = converter.convert(subject);
		Assert.assertEquals(4, item.getItemPropertyIds().size());
		Assert.assertEquals(ID, item.getItemProperty(TestConstants.SUBJECT_COLS_ID).getValue());
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(SUBJECT_DESC, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());
		Assert.assertEquals(subject.created(), item.getItemProperty(TestConstants.SUBJECT_COLS_DATE).getValue());

	}

	@Test
	public final void toNulls() {
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		Assert.assertNotNull(subject.created());
		final Item item = converter.convert(subject);
		Assert.assertEquals(-1L, item.getItemProperty(TestConstants.SUBJECT_COLS_ID).getValue());
		Assert.assertEquals(EMPTY_STRING, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(EMPTY_STRING, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());
		Assert.assertEquals(subject.created(), item.getItemProperty(TestConstants.SUBJECT_COLS_DATE).getValue());
	}

	@Test
	public final void toWebCols() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Converter<Subject, Item> converter = new DomainToItemConverterImpl(new Enum[] { TestConstants.SUBJECT_COLS_NAME });
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		ReflectionTestUtils.setField(subject, DESCRIPTION_FIELD, SUBJECT_DESC);
		ReflectionTestUtils.setField(subject, ID_FIELD, ID);
		Assert.assertNotNull(subject.created());

		final Item item = converter.convert(subject);

		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());

		Assert.assertEquals(1, item.getItemPropertyIds().size());
	}

	@Test
	public final void toWebChild() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Converter<Subject, Item> converter = new DomainToItemConverterImpl(SubjectTestCols.class).withChild(SubjectTestCols.Customer);
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		final Customer customer = BeanUtils.instantiateClass(CustomerImpl.class);
		ReflectionTestUtils.setField(customer, "id", ID);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		ReflectionTestUtils.setField(subject, CUSTOMER_FIELD, customer);

		final Item item = converter.convert(subject);

		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(SubjectTestCols.Name).getValue());

		Assert.assertEquals(ID, item.getItemProperty(SubjectTestCols.Customer).getValue());

	}

	@Test
	public final void toWebChildEntityNull() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Converter<Subject, Item> converter = new DomainToItemConverterImpl(SubjectTestCols.class).withChild(SubjectTestCols.Customer);

		final Item item = converter.convert(BeanUtils.instantiateClass(SubjectImpl.class));

		Assert.assertEquals(SubjectTestCols.Customer.nvl(), item.getItemProperty(SubjectTestCols.Customer).getValue());

	}

	@Test
	public final void toWebChildIdNull() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Converter<Subject, Item> converter = new DomainToItemConverterImpl(SubjectTestCols.class).withChild(SubjectTestCols.Customer);

		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		final Customer customer = BeanUtils.instantiateClass(CustomerImpl.class);

		ReflectionTestUtils.setField(subject, CUSTOMER_FIELD, customer);

		final Item item = converter.convert(subject);
		Assert.assertEquals(SubjectTestCols.Customer.nvl(), item.getItemProperty(SubjectTestCols.Customer).getValue());

	}

	@Test
	public final void toWebChildNonEntity() {

		final MySubject subject = new MySubject();
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Converter<MySubject, Item> converter = new DomainToItemConverterImpl(SubjectTestCols.class).withChild(SubjectTestCols.Customer);

		final MyCustomer customer = new MyCustomer();

		ReflectionTestUtils.setField(subject, CUSTOMER_FIELD, customer);

		final Item item = converter.convert(subject);

		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(SubjectTestCols.Name).getValue());

		Assert.assertEquals(SubjectTestCols.Customer.nvl(), item.getItemProperty(SubjectTestCols.Customer).getValue());

	}

	enum SubjectTestCols implements TableContainerColumns {
		Name, Customer;
		@Override
		public boolean visible() {
			return false;
		}

		@Override
		public Class<?> target() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean sortable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String orderBy() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object nvl() {
			// TODO Auto-generated method stub
			return -1L;
		}

	}

	class MySubject {
		String name;

		MyCustomer customer;

	}

	class MyCustomer {

	}

}

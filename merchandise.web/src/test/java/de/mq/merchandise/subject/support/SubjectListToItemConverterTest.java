package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;





import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;





import com.vaadin.data.Item;

import de.mq.merchandise.subject.Subject;

public class SubjectListToItemConverterTest {

	private static final String SUBJECT_NAME = "subjectName";

	private static final long ID = 19680528L;

	private final Converter<Collection<Subject>, Container> converter = new SubjectListToItemConverterImpl();
	
	private final Collection<Subject> source = new ArrayList<>();
	private final Subject subject = Mockito.mock(Subject.class);
	
	@Before
	public final void setup() {
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));
		Mockito.when(subject.name()).thenReturn(SUBJECT_NAME);
		source.add(subject);
	}
	
	@Test
	public final void convert() {
		final Container container = converter.convert(source);
		Assert.assertEquals(1, container.getItemIds().size());
		Assert.assertTrue(container.getItemIds().stream().findAny().isPresent());
		final Item item = container.getItem(container.getItemIds().stream().findAny().get());
		Assert.assertEquals(1, item.getItemPropertyIds().size());
		Assert.assertTrue(item.getItemPropertyIds().stream().findAny().isPresent());
		Assert.assertEquals(SubjectCols.Name, item.getItemPropertyIds().stream().findAny().get());
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(item.getItemPropertyIds().stream().findAny().get()).getValue());
	}
	
}

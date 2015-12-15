package de.mq.merchandise.subject.support;

import java.util.Arrays;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class ItemToCommercialSubjectConverterTest {

	private static final Long ID = 19680528L;

	private final Converter<Item,CommercialSubject> converter = new ItemToCommercialSubjectConverterImpl();
	
	final Item item = new PropertysetItem();
	
	@Before
	public final void setup() {
		
		Arrays.asList(CommercialSubjectCols.values()).stream().forEach(col -> {
			if(col.target().equals(String.class)) {
				item.addItemProperty(col, new ObjectProperty<>(col.name().toLowerCase()));
			} else if(col.target().equals(Long.class) ) {
				item.addItemProperty(col, new ObjectProperty<>(ID));
			}
			
			
		} );
	
	}
	
	@Test
	public final void convert() {
	
		
		final CommercialSubject result = converter.convert(item);
		Assert.assertEquals(CommercialSubjectCols.Name.name().toLowerCase(), result.name());
		Assert.assertEquals(1, result.commercialSubjectItems().size());
		Assert.assertEquals(CommercialSubjectCols.ItemName.name().toLowerCase(), result.commercialSubjectItems().stream().findFirst().get().name());
		
		Assert.assertEquals(CommercialSubjectCols.SubjectName.name().toLowerCase(), result.commercialSubjectItems().stream().findFirst().get().subject().name());
		Assert.assertEquals(CommercialSubjectCols.SubjectDesc.name().toLowerCase(), result.commercialSubjectItems().stream().findFirst().get().subject().description());
		
		Assert.assertTrue(result.id().isPresent());
		Assert.assertEquals(ID, result.id().get());
		
		Assert.assertFalse(result.commercialSubjectItems().stream().findFirst().get().subject().id().isPresent());
	}
	
	
	@Test
	public final void convertNulls() {
		final Item item = Mockito.mock(Item.class);
		final CommercialSubject result = converter.convert(item);
		Assert.assertNull(result.name());
		Assert.assertEquals(1, result.commercialSubjectItems().size());
		Assert.assertNull(result.commercialSubjectItems().stream().findFirst().get().name());
		
		Assert.assertNull(result.commercialSubjectItems().stream().findFirst().get().subject().name());
		Assert.assertNull(result.commercialSubjectItems().stream().findFirst().get().subject().description());
		
		Assert.assertFalse(result.id().isPresent());
	
		
		Assert.assertFalse(result.commercialSubjectItems().stream().findFirst().get().subject().id().isPresent());
	
	}
	
	
}

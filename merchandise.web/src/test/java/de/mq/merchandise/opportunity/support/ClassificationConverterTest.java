package de.mq.merchandise.opportunity.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class ClassificationConverterTest {
	
	
	private static final String DESCRIPTION = "DESCRIPTION";

	private final Converter converter = new ClassificationConverter();
	
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	private final UIComponent uiComponent = Mockito.mock(UIComponent.class);
	
	@Test
	public final void getAsObject() {
		Assert.assertNull(converter.getAsObject(facesContext, uiComponent, "VALUE"));
	}
	
	@Test
	public final void getAsString() {
		final Classification classification = Mockito.mock(Classification.class);
		Mockito.when(classification.description()).thenReturn(DESCRIPTION);
		Assert.assertEquals(DESCRIPTION, converter.getAsString(facesContext, uiComponent, classification));
	}
	
	@Test
	public final void getAsStringWrongType() {
		Assert.assertEquals(ClassificationConverter.UNDEFINED, converter.getAsString(facesContext, uiComponent, null));
	}

}

package de.mq.merchandise.opportunity.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class ConditionConverterTest {
	
	private static final String STRING_VALUE = "VALUE";
	private static final String SUBJECT_NAME = "subject-name";
	private final Converter converter = new  ConditionConverterImpl();
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	private final UIComponent uiComponent = Mockito.mock(UIComponent.class);
	
	
	@Test
	public final void getAsObject() {
		Assert.assertNull(converter.getAsObject(facesContext, uiComponent, STRING_VALUE));
	}
	
	@Test
	public final void getAsStringCommercialRelation() {
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialRelation.commercialSubject()).thenReturn(commercialSubject);
		Mockito.when(commercialSubject.name()).thenReturn(SUBJECT_NAME);
		Assert.assertEquals(SUBJECT_NAME, converter.getAsString(facesContext, uiComponent, commercialRelation));
	}
	
	@Test
	public final void getAsStringCondition() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.PricePerUnit);
		Assert.assertEquals(ConditionType.PricePerUnit.name(), converter.getAsString(facesContext, uiComponent, condition));
	}
	
	@Test
	public final void getAsStringToString() {
		Assert.assertEquals(STRING_VALUE, converter.getAsString(facesContext, uiComponent, STRING_VALUE));
	}

}

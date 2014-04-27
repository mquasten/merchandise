package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.opportunity.support.RuleInstance;
import de.mq.merchandise.rule.Rule;

public class RuleInstanceToParameterConverterTest {
	
	private static final String PAR_HOT_SCORE_VALUE = "10";

	private static final String PAR_NAME_VALUE = "Kylie";

	private static final String PAR_HOT_SCORE_KEY = "hotScore";

	private static final String PAR_NAME_KEY = "name";

	final RuleInstanceToParameterConverter ruleInstanceToParameterConverter = new RuleInstanceToParameterConverter();
	
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	
	private UIComponent uiComponent = Mockito.mock(UIComponent.class);
	
	@Test
	public final void getAsObject() {
		Assert.assertNull(ruleInstanceToParameterConverter.getAsObject(facesContext, uiComponent, "dontLetMeGetMe"));
	}
	
	@Test
	public final void getAsStringNonRuleInstance() {
		Assert.assertEquals(RuleInstanceToParameterConverter.UNDEFINED, ruleInstanceToParameterConverter.getAsString(facesContext, uiComponent, Mockito.mock(Rule.class)));
	}
	
	@Test
	public final void getAsString() {
		final RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		final List<String> names = new ArrayList<>();
		names.add(PAR_NAME_KEY);
		names.add(PAR_HOT_SCORE_KEY);
		Mockito.when(ruleInstance.parameterNames()).thenReturn(names);
		
		Mockito.when(ruleInstance.parameter(PAR_NAME_KEY)).thenReturn(PAR_NAME_VALUE);
		Mockito.when(ruleInstance.parameter(PAR_HOT_SCORE_KEY)).thenReturn(PAR_HOT_SCORE_VALUE);
		Assert.assertEquals(String.format("%s=%s %s=%s", PAR_NAME_KEY, PAR_NAME_VALUE, PAR_HOT_SCORE_KEY, PAR_HOT_SCORE_VALUE), ruleInstanceToParameterConverter.getAsString(facesContext, uiComponent,ruleInstance));
	}

}

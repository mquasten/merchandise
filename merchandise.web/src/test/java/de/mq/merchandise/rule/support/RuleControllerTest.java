package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.Paging;

public class RuleControllerTest {
	
	private static final String STATE = "state";
	private static final long ID = 19680528L;
	private final Rule rule = Mockito.mock(Rule.class);
	private final RuleModelAO ruleModelAO = Mockito.mock(RuleModelAO.class);
	private final RuleService ruleService = Mockito.mock(RuleService.class);
	
	private final RuleControllerImpl ruleController = new RuleControllerImpl(ruleService);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	private final PagingAO pagingAO = Mockito.mock(PagingAO.class);
	private final Paging paging = Mockito.mock(Paging.class);
	
	private final RuleAO ruleAO = Mockito.mock(RuleAO.class);
	
	private final List<Rule> rules = new ArrayList<>();
	private final List<RuleAO> ruleAOs = new ArrayList<>();

	private final DocumentModelAO documentModelAO = Mockito.mock(DocumentModelAO.class);
	
	@Test
	public final void rules() {
	
		rules.add(rule);
		Mockito.when(ruleService.rules(customer, "%", paging)).thenReturn(rules);
		Mockito.when(pagingAO.getPaging()).thenReturn(paging);
		
		
		Mockito.when(ruleModelAO.getPaging()).thenReturn(pagingAO);
		
		ruleController.rules(ruleModelAO, customer);
		
		Mockito.verify(ruleModelAO).setRules(rules);
		
	}
	
	@Test
	public final void rulesWithPattern() {
		Mockito.when(ruleModelAO.getPattern()).thenReturn("hotScore");
		
		rules.add(rule);
		Mockito.when(ruleService.rules(customer, "hotScore%", paging)).thenReturn(rules);
		Mockito.when(pagingAO.getPaging()).thenReturn(paging);
		
		
		Mockito.when(ruleModelAO.getPaging()).thenReturn(pagingAO);
		
		ruleController.rules(ruleModelAO, customer);
		
		Mockito.verify(ruleModelAO).setRules(rules);
		
	}
	
	@Test
	public final void updateSelectionExistsInResult() {
		Mockito.when(ruleAO.getRule()).thenReturn(rule);
		ruleAOs.add(ruleAO);
		
		Mockito.when(ruleModelAO.getRules()).thenReturn(ruleAOs);
		Mockito.when(ruleModelAO.getSelected()).thenReturn(ruleAO);
		
		ruleController.updateSelection(ruleModelAO);
		
		Mockito.verify(ruleModelAO, Mockito.times(0)).setSelected(null);
	}
	
	
	@Test
	public final void updateSelection() {
	
		final Rule otherRule = Mockito.mock(Rule.class);
		final RuleAO otherRuleAO =  Mockito.mock(RuleAO.class);
		Mockito.when(otherRuleAO.getRule()).thenReturn(otherRule);
		
		Mockito.when(ruleAO.getRule()).thenReturn(rule);
		ruleAOs.add(ruleAO);
		
		
		
		Mockito.when(ruleModelAO.getRules()).thenReturn(ruleAOs);
		Mockito.when(ruleModelAO.getSelected()).thenReturn(otherRuleAO);
		
		ruleController.updateSelection(ruleModelAO);
		
		Mockito.verify(ruleModelAO, Mockito.times(1)).setSelected(null);
	}
	
	@Test
	public final void initRule() {
	
		Mockito.when(ruleService.read(ID)).thenReturn(rule);
		ruleController.initRuleAO(ruleAO, documentModelAO, ID, STATE);
		
		Mockito.verify(ruleAO,  Mockito.times(1)).setParentState(STATE);
		Mockito.verify(ruleAO, Mockito.times(1)).setRule(rule);
		Mockito.verify(documentModelAO, Mockito.times(1)).setDocument(rule);
	}
	
	
	@Test
	public final void initRuleIDNull() {
		Mockito.when(ruleService.read(ID)).thenReturn(rule);
		ruleController.initRuleAO(ruleAO, documentModelAO, null, STATE);
		
		Mockito.verify(ruleAO,  Mockito.times(1)).setParentState(STATE);
		Mockito.verify(ruleAO, Mockito.times(0)).setRule(rule);
		Mockito.verify(documentModelAO, Mockito.times(0)).setDocument(rule);

	}
	
	@Test
	public final void save() {
		
		Mockito.when(ruleAO.getParentState()).thenReturn(STATE);
		Mockito.when(ruleAO.getRule()).thenReturn(rule);
		
		Assert.assertEquals(String.format(RuleControllerImpl.RETURN_URL,STATE), ruleController.save(ruleAO));
		
		Mockito.verify(ruleService).createOrUpdate(rule);
		
	}
	

}

package de.mq.merchandise.rule.support;

import groovy.lang.GroovyObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.util.CollectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.opportunity.support.RuleInstance;
import de.mq.merchandise.opportunity.support.RuleOperations;
import de.mq.merchandise.rule.ParameterNamesAware;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.Paging;

public class RuleControllerTest {
	
	private static final String PARAMETER_VALUE = "10";
	private static final String PARAMETER_NAME = "hotScore";
	private static final int PRIORITY = 4711;
	private static final String STATE = "state";
	private static final long ID = 19680528L;
	private final Rule rule = Mockito.mock(Rule.class);
	private final RuleModelAO ruleModelAO = Mockito.mock(RuleModelAO.class);
	private final RuleService ruleService = Mockito.mock(RuleService.class);
	
	private SourceFactoryImpl sourceFactory = Mockito.mock(SourceFactoryImpl.class);
	
	private final RuleControllerImpl ruleController = new RuleControllerImpl(ruleService,sourceFactory);
	
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
		Mockito.when(ruleModelAO.getPattern()).thenReturn(PARAMETER_NAME);
		
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
	
	@Test
	public final void ruleItems() {
		final RuleModelAO  ruleModelAO = Mockito.mock(RuleModelAO.class);
		final List<RuleAO> ruleAOs = new ArrayList<>();
		Mockito.when(ruleModelAO.getRules()).thenReturn(ruleAOs);
		final RuleAO ruleAO = Mockito.mock(RuleAO.class);
		Mockito.when(ruleAO.getId()).thenReturn("19680528");
		Mockito.when(ruleAO.getName()).thenReturn(PARAMETER_NAME);
		ruleAOs.add(ruleAO);
		
		final List<SelectItem> results = ruleController.ruleItems(ruleModelAO);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(ruleAO.getId(), results.iterator().next().getValue());
		Assert.assertEquals(ruleAO.getName(), results.iterator().next().getLabel());
	}
	
	@Test
	public final void selectedId() {
		final Rule rule = Mockito.mock(Rule.class);
		Mockito.when(rule.hasId()).thenReturn(true);
		Mockito.when(rule.id()).thenReturn(ID);
		Assert.assertEquals(ID, (long) ruleController.selectedId(rule));
		Mockito.verify(rule).hasId();
		Mockito.verify(rule).id();
	}
	
	@Test
	public final void selectedIdNoId() {
		final Rule rule = Mockito.mock(Rule.class);
		
		Assert.assertNull(ruleController.selectedId(rule));
		
		Mockito.verify(rule).hasId();
		Mockito.verify(rule, Mockito.times(0)).id();
	
	}
	
	@Test
	public final void instances() {
		final RuleOperations ruleOperations = Mockito.mock(RuleOperations.class);
		final List<RuleInstance>  ruleInstances = new ArrayList<>();
		final RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		ruleInstances.add(ruleInstance);
		Mockito.when(ruleOperations.ruleInstances()).thenReturn(ruleInstances);
		final Collection<?> results = ruleController.instances(ruleOperations);
		Assert.assertTrue(results instanceof SimpleMapDataModel);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(ruleInstance, results.iterator().next());
			
	}
	
	@Test
	public final void selected() {
		Rule matchingRule = Mockito.mock(Rule.class);
		RuleInstance matchingRuleInstance = Mockito.mock(RuleInstance.class);
		Mockito.when(matchingRuleInstance.rule()).thenReturn(matchingRule);
		Rule rule = Mockito.mock(Rule.class);
		RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		Mockito.when(ruleInstance.rule()).thenReturn(rule);
		final RuleOperations ruleOperations = Mockito.mock(RuleOperations.class);
		final List<RuleInstance> ruleInstances = new ArrayList<>();
		ruleInstances.add(ruleInstance);
		ruleInstances.add(matchingRuleInstance);
		Mockito.when(ruleOperations.ruleInstances()).thenReturn(ruleInstances);
		
		Assert.assertEquals(matchingRuleInstance, ruleController.selected(matchingRule, ruleOperations));
		
		Assert.assertNull( ruleController.selected(Mockito.mock(Rule.class), ruleOperations));
	}
	
	
	@Test
	public final void addRuleInstance() {
		final RuleInstanceAO ruleInstanceAO = Mockito.mock(RuleInstanceAO.class);
		final RuleAO ruleAO = Mockito.mock(RuleAO.class);
		final Rule rule = Mockito.mock(Rule.class);
		Mockito.when(ruleInstanceAO.getRule()).thenReturn(ruleAO);
		Mockito.when(ruleAO.getRule()).thenReturn(rule);
		final RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		Mockito.when(ruleInstance.priority()).thenReturn(11);
		Mockito.when(ruleInstanceAO.getRuleInstance()).thenReturn(ruleInstance);
		RuleOperations parent = Mockito.mock(RuleOperations.class);
		Mockito.when(ruleInstanceAO.getParent()).thenReturn(parent);
		
		Mockito.when(parent.ruleInstance(rule)).thenReturn(ruleInstance);
		
		final List<ParameterAO> params = new ArrayList<>();
		params.add(Mockito.mock(ParameterAO.class));
		Mockito.when(params.get(0).getName()).thenReturn(PARAMETER_NAME);
		Mockito.when(params.get(0).getValue()).thenReturn(PARAMETER_VALUE);
		
		Mockito.when(ruleInstanceAO.getParameter()).thenReturn(params);
		
		ruleController.addRuleInstance(ruleInstanceAO);
		
		
		
		Mockito.verify(parent).assign(rule, ruleInstance.priority());
		Mockito.verify(parent).ruleInstance(rule);
		Mockito.verify(ruleInstance).assign(params.get(0).getName(), params.get(0).getValue());
		
	}
	
	
	@Test
	public final void deleteRuleInstance() {
		
		final RuleInstanceAO ruleInstanceAO = Mockito.mock(RuleInstanceAO.class);
		final RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		Mockito.when(ruleInstanceAO.getRuleInstance()).thenReturn(ruleInstance);
		RuleOperations parent = Mockito.mock(RuleOperations.class);
		Mockito.when(ruleInstanceAO.getParent()).thenReturn(parent);
		final RuleAO ruleAO = Mockito.mock(RuleAO.class);
		final Rule rule = Mockito.mock(Rule.class);
		Mockito.when(ruleInstanceAO.getRule()).thenReturn(ruleAO);
		Mockito.when(ruleAO.getRule()).thenReturn(rule);
		
		
		ruleController.deleteRuleInstance(ruleInstanceAO);
		
		Mockito.verify(ruleInstance).clearAllParameter();
		Mockito.verify(parent).remove(rule);
		Mockito.verify(ruleInstanceAO).setPriority(null);
		final ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass(Rule.class);
		Mockito.verify(ruleAO).setRule(ruleCaptor.capture());
		
		Assert.assertEquals(RuleImpl.class, ruleCaptor.getValue().getClass());
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void assignSelected() {
		Rule rule = new RuleImpl(customer, "ArtitsHotScore");
		final RuleInstanceAO ruleInstanceAO = Mockito.mock(RuleInstanceAO.class);
		Mockito.when(ruleService.read(ID)).thenReturn(rule);
		final RuleOperations ruleOperations = Mockito.mock(RuleOperations.class);
		Mockito.when(ruleInstanceAO.getParent()).thenReturn(ruleOperations);
		ParameterNamesAware<String> groovyObject = Mockito.mock(ParameterNamesAware.class);
		Mockito.when(sourceFactory.create(ID)).thenReturn(groovyObject);
		final String[] params = new String[] { PARAMETER_NAME};
		Mockito.when(groovyObject.parameters()).thenReturn(params);
		Mockito.when(ruleOperations.hasRule(rule)).thenReturn(true);
		RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		Mockito.when(ruleOperations.ruleInstance(rule)).thenReturn(ruleInstance);
		Mockito.when(ruleInstance.parameterNames()).thenReturn(CollectionUtils.arrayToList(params));
		Mockito.when(ruleInstance.priority()).thenReturn(PRIORITY);
		Mockito.when(ruleInstance.parameter(PARAMETER_NAME)).thenReturn(PARAMETER_VALUE);
		ruleController.assignSelected(ID, ruleInstanceAO);
		
		ArgumentCaptor<RuleInstance> ruleInstanceArgumentCaptor = ArgumentCaptor.forClass(RuleInstance.class);
		Mockito.verify(ruleInstanceAO).setRuleInstance(ruleInstanceArgumentCaptor.capture());
		
		
		
		final RuleInstance ruleInstanceResult = ruleInstanceArgumentCaptor.getValue();
		Assert.assertEquals(PRIORITY, ruleInstanceResult.priority());
		Assert.assertEquals(PARAMETER_VALUE, ruleInstanceResult.parameter(PARAMETER_NAME));
		Assert.assertEquals(1, ruleInstanceResult.parameterNames().size());
		Assert.assertEquals(PARAMETER_NAME, ruleInstanceResult.parameterNames().get(0));
		
	}
	
	

}

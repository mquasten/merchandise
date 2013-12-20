package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;



import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;

import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.Parameter;

public class RuleRepositoryTest {
	
	private static final String NAME = "calculateArtistsHotScore";

	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private final PagingUtil pagingUtil = Mockito.mock(PagingUtil.class);
	
	private final Customer customer = Mockito.mock(Customer.class);
	private final Paging paging = Mockito.mock(Paging.class);
	
	private RuleRepository ruleRepository = new RuleRepositoryImpl(entityManager, pagingUtil);
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public final void forNamePattern() {
		
		Mockito.when(customer.id()).thenReturn(19680528L);
		final ArgumentCaptor<EntityManager> entityManagerCaptor = ArgumentCaptor.forClass(EntityManager.class);
	
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<Paging> pagingCaptor = ArgumentCaptor.forClass(Paging.class);
		final ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
		
		
		final ArgumentCaptor<Parameter> patternParameterCaptor = ArgumentCaptor.forClass(Parameter.class);
	
		final ArgumentCaptor<Parameter> customerIdParameterCaptor = ArgumentCaptor.forClass(Parameter.class);
		
		final Rule rule = Mockito.mock(Rule.class);
		final Collection<Rule> rules = new ArrayList<>();
		rules.add(rule);
		
		Mockito.when(pagingUtil.countAndQuery(entityManagerCaptor.capture(), classCaptor.capture(), pagingCaptor.capture(), queryCaptor.capture(), patternParameterCaptor.capture(), customerIdParameterCaptor.capture())).thenReturn(rules);
		
		final Collection<Rule> results = ruleRepository.forNamePattern(customer, NAME, paging);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(rule, results.iterator().next());
		Assert.assertEquals(rules, results);
		Assert.assertEquals(entityManager, entityManagerCaptor.getValue());
		Assert.assertEquals(paging, pagingCaptor.getValue());
		Assert.assertEquals(RuleRepository.RULE_FOR_NAME_PATTERN, queryCaptor.getValue());
		Assert.assertEquals(RuleRepository.PARAMETER_RULE_NAME, ((Parameter<String>) patternParameterCaptor.getValue()).name());
		Assert.assertEquals(NAME, ((Parameter<String>) patternParameterCaptor.getValue()).value());
		Assert.assertEquals(RuleRepository.PARAMETER_CUSTOMER_ID, ((Parameter<Long>) customerIdParameterCaptor.getValue()).name());
		Assert.assertEquals(customer.id(), (long) ((Parameter<Long>) customerIdParameterCaptor.getValue()).value());
	}
	
	@Test
	public final void clazz() {
		 Assert.assertEquals(RuleImpl.class, new RuleRepositoryImpl(entityManager, pagingUtil).entityImplementationClass());
	}
	
	@Test
	public final void defaultConstructor() {
		final RuleRepository ruleRepository = new RuleRepositoryImpl();
		Assert.assertNull(ReflectionTestUtils.getField(ruleRepository, "pagingUtil"));
		Assert.assertNull(ReflectionTestUtils.getField(ruleRepository, "entityManager"));
	}

}

package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

public class Opportunity2TSStringConverterTest {
	
	private static final String KEYWORD = "8,8-cm-KwK 36 L/56";
	final Converter<Opportunity,String> converter =  new Opportunity2TSStringConverterImpl();
	
	@Test
	public final void convert() {
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		Collection<String> keywords= new ArrayList<>();
		keywords.add(KEYWORD);
		Mockito.when(opportunity.keyWords()).thenReturn(keywords);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(opportunity.customer()).thenReturn(customer);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(customer.person()).thenReturn(person);
		Mockito.when(person.name()).thenReturn("Kupp");
		Mockito.when(opportunity.name()).thenReturn("Flugabwehrkanone 8.8 cm");
		Mockito.when(opportunity.description()).thenReturn("auch im Tiger");
		Mockito.when(opportunity.kind()).thenReturn(Kind.ProductOrService);
		Assert.assertEquals(String.format("%s %s %s %s %s", opportunity.name(), opportunity.description(), person.name(),KEYWORD, opportunity.kind() ), converter.convert(opportunity));
		
	}
	
	@Test
	public final void convertAllNull() {
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		final Customer customer = Mockito.mock(Customer.class);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(opportunity.customer()).thenReturn(customer);
		Mockito.when(customer.person()).thenReturn(person);
		Assert.assertEquals(0, converter.convert(opportunity).trim().length());
	}

}

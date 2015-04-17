package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Set;

import java.util.stream.Collectors;

import junit.framework.Assert;

import org.junit.Test;



public class ConditionDataTypeTest {
	
	@Test
	public final void create() {
		Arrays.asList(ConditionDataType.values()).forEach(v -> Assert.assertEquals(v, ConditionDataType.valueOf(v.name())));
	}
	
	@Test
	public final void targetTypes() {
		
		final Set<Class<?>>  results =  Arrays.asList(ConditionDataType.values()).stream().map(v -> v.targetClass()).collect(Collectors.toSet());
		Assert.assertTrue(results.contains(String.class));
		Assert.assertTrue(results.contains(Long.class));
		Assert.assertTrue(results.contains(Double.class));
	}

}

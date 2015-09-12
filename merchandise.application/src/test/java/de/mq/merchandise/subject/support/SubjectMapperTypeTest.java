package de.mq.merchandise.subject.support;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.subject.support.MapperQualifier.MapperType;

public class SubjectMapperTypeTest {
	
	@Test
	public final void values(){
		Arrays.asList(MapperType.values()).forEach(col -> Assert.assertEquals(col, MapperType.valueOf(col.name())));
	}

}

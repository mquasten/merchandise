package de.mq.merchandise.subject.support;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;

public class SubjectMapperTypeTest {
	
	@Test
	public final void values(){
		Arrays.asList(SubjectMapperType.values()).forEach(col -> Assert.assertEquals(col, SubjectMapperType.valueOf(col.name())));
	}

}

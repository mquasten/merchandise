package de.mq.merchandise.subject.support;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class CommercialSubjectModelQualifierTest {
	
	@Test
	public final void create() {
	
		Arrays.asList(CommercialSubjectModelQualifier.Type.values()).stream().forEach(col -> Assert.assertEquals(col , CommercialSubjectModelQualifier.Type.valueOf(col.name())));
	}
	
	public final void size() {
		Assert.assertEquals(13, CommercialSubjectModelQualifier.Type.values().length);
	}

}

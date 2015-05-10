package de.mq.merchandise.subject.support;

import java.util.Arrays;




import org.junit.Assert;
import org.junit.Test;

public class SubjectModelQualifierTest {
	
	@Test
	public final void create() {
		Arrays.asList(SubjectModelQualifier.Type.values()).forEach(qualifier -> Assert.assertEquals(qualifier, SubjectModelQualifier.Type.valueOf(qualifier.name()) ));
	}

}

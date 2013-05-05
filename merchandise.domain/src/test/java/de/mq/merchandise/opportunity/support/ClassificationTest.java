package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ClassificationTest {
	
	private static final String DESCRIPTION = "Gas Wasser Scheisse";
	private static final String ID = "A-01";

	@Test
	public final void create() {
		final Classification classification = new AbstractClassification() {
			private static final long serialVersionUID = 1L;
		};
		
		final Classification parent = Mockito.mock(Classification.class);
		ReflectionTestUtils.setField(classification, "id", ID);
		ReflectionTestUtils.setField(classification, "description", DESCRIPTION);
		ReflectionTestUtils.setField(classification, "parent", parent);
		
		Assert.assertEquals(ID, classification.id());
		Assert.assertEquals(DESCRIPTION, classification.description());
		Assert.assertEquals(parent, classification.parent());
		
	}
	
	@Test
	public final void hash() {
		Assert.assertEquals(DESCRIPTION.hashCode(), newClassification(DESCRIPTION).hashCode());
	}
	
	@Test
	public final void equals() {
		Assert.assertTrue(newClassification(DESCRIPTION).equals(newClassification(DESCRIPTION)));
		Assert.assertFalse(newClassification(DESCRIPTION).equals(newClassification("dontLetMeGetMe")));	
	}
	
	@Test
	public final void productClassification() {
		Assert.assertNotNull(new ProductClassificationImpl());
	}
	
	@Test
	public final void activityClassification() {
		Assert.assertNotNull(new ActivityClassificationImpl());
	}

	private Classification newClassification(final String description) {
		final Classification classification = new AbstractClassification() {
			private static final long serialVersionUID = 1L;
		};
		ReflectionTestUtils.setField(classification, "description", description);
		return classification;
	}

}

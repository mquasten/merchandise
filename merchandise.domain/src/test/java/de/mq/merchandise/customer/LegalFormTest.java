package de.mq.merchandise.customer;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.LegalForm;


public class LegalFormTest {
	@Test
	public final void size() {
		Assert.assertEquals(12, LegalForm.values().length);
	}
	
	@Test
	public final void legalForm() {
		for(final LegalForm legalForm : LegalForm.values()){
			Assert.assertNotNull(LegalForm.valueOf(legalForm.name()));
		}
	}

}

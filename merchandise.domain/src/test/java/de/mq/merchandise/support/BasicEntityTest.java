package de.mq.merchandise.support;

import javax.persistence.Id;

import junit.framework.Assert;

import org.junit.Test;

public class BasicEntityTest {
	
	private static final Long ID = 19680528L;

	@Test
	public final void idPresent() {
		final BasicEntity basicEntity = new BasicEntity() { @Id private Long id = ID; };
		Assert.assertTrue(basicEntity.id().isPresent());
		Assert.assertEquals(ID, basicEntity.id().get());
		
	}
	
	@Test
	public final void IdNotPresent() {
		final BasicEntity basicEntity =   new BasicEntity(){ @Id private Long id; }; 
		Assert.assertFalse(basicEntity.id().isPresent());
	}

}

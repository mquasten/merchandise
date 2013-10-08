package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Test;

public class AttachementControllerTest {
	
	private static final String EXT = "jpg";
	private static final String DOCUMENT = "kylie";
	private static final long ID = 19680528;
	private static final String ENTITY = "opportunities";
	private final AttachementControllerImpl attachementController = new AttachementControllerImpl();
	
	@Test
	public final void content() {
		Assert.assertEquals(String.format(AttachementControllerImpl.URL,ENTITY,  ID, DOCUMENT , EXT), attachementController.content(ENTITY, ID, DOCUMENT, EXT));
		
	}

}

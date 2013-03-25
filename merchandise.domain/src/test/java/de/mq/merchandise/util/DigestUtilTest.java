package de.mq.merchandise.util;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.util.DigestUtil;
import de.mq.merchandise.util.DigestUtil.Algorithm;

public class DigestUtilTest {
	
	@Test
	public final void digest() {
		Assert.assertEquals(32, DigestUtil.digestAsHex("pokerface", Algorithm.MD5).length());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void invalidAlogoritm() {
		DigestUtil.digestAsHex("pokerface", "dontLetmegetMe");
	}
	
	@Test
	public final void Enums() {
		Assert.assertEquals(2, DigestUtil.Algorithm.values().length);
		
		
	}
	@Test
	public final void coverage() {
		Assert.assertNotNull(new DigestUtil());
	}

}

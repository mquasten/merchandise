package de.mq.merchandise.customer.support;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.DigestUtils;

import de.mq.merchandise.customer.support.Digest.Algorithm;

public class DigestTest {
	
	static final String TEXT = "bornThisWay";
	
	@Test
	public final void md5() {
		final  Digest digest = new DigestImpl();
	    digest.assignDigest(TEXT, Algorithm.MD5);
	    Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
	}
	
	@Test
	public final void defaultAlgorithm() {
		final  Digest digest = new DigestImpl();
	    digest.assignDigest(TEXT);
		
	    Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void wrongAlgorithm() {
		new DigestImpl().digestAsHex(TEXT, "dontLetMeGetMe");
	}
	
	@Test
	public final void non() {
		final  Digest digest = new DigestImpl();
		digest.assignDigest(TEXT, Algorithm.NON);
		Assert.assertEquals(new BigInteger(TEXT.getBytes()).toString(16).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
		
	}
	
	@Test
	public final void check() {
		final  Digest digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest", DigestUtils.md5DigestAsHex(TEXT.getBytes()));
		ReflectionTestUtils.setField(digest, "algorithm", Algorithm.MD5);
		Assert.assertTrue(digest.check(TEXT));
		Assert.assertFalse(digest.check("pokerface"));
	}

}

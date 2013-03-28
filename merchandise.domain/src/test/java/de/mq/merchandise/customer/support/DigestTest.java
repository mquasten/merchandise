package de.mq.merchandise.customer.support;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.DigestUtils;

import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.Digest.Algorithm;

public class DigestTest {
	
	static final String TEXT = "bornThisWay";
	
	@Test
	public final void assignMd5() {
		final  Digest digest = new DigestImpl();
	    digest.assignDigest(TEXT, Algorithm.MD5);
	    Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
	    Assert.assertTrue((boolean) ReflectionTestUtils.getField(digest, "crypted"));
	    Assert.assertEquals(Algorithm.MD5, ReflectionTestUtils.getField(digest, "algorithm"));
	}
	
	@Test
	public final void defaultAlgorithm() {
		final  Digest digest = new DigestImpl();
	    digest.assignDigest(TEXT);
		
	    Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
	    Assert.assertTrue( (boolean) ReflectionTestUtils.getField(digest, "crypted"));
	    Assert.assertEquals(Algorithm.MD5, ReflectionTestUtils.getField(digest, "algorithm"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void wrongAlgorithm() {
		new DigestImpl().digestAsHex(TEXT, "dontLetMeGetMe");
	}
	
	@Test
	public final void assignNon() {
		final  Digest digest = new DigestImpl();
		digest.assignDigest(TEXT, Algorithm.NON);
		Assert.assertEquals(new BigInteger(TEXT.getBytes()).toString(16).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
		Assert.assertTrue((boolean) ReflectionTestUtils.getField(digest, "crypted"));
		Assert.assertEquals(Algorithm.NON, ReflectionTestUtils.getField(digest, "algorithm"));
		
	}
	
	@Test
	public final void assignUncrypted() {
		final  Digest digest = new DigestImpl();
		digest.assignDigest(TEXT, Algorithm.UNCRYPTED);
		Assert.assertEquals(TEXT, ReflectionTestUtils.getField(digest, "digest") );
		Assert.assertTrue((boolean) ReflectionTestUtils.getField(digest, "crypted"));
		Assert.assertEquals(Algorithm.UNCRYPTED, ReflectionTestUtils.getField(digest, "algorithm") );
	}
	
	@Test
	public final void checkDigest() {
		final  Digest digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest", DigestUtils.md5DigestAsHex(TEXT.getBytes()));
		ReflectionTestUtils.setField(digest, "algorithm", Algorithm.MD5);
		ReflectionTestUtils.setField(digest, "crypted", true);
		Assert.assertTrue(digest.check(TEXT));
		Assert.assertFalse(digest.check("pokerface"));
	}
	
	@Test
	public final void checkNonDigest() {
		final  Digest digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest", TEXT);
		Assert.assertTrue(digest.check(TEXT));
		Assert.assertFalse(digest.check("dontLetMeGetMe"));
	}
	
	@Test
	public final void checkUncrypted() {
		final  Digest digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest", TEXT);
		ReflectionTestUtils.setField(digest, "algorithm", Algorithm.UNCRYPTED);
		Assert.assertTrue(digest.check(TEXT));
		Assert.assertFalse(digest.check("dontLetMeGetMe"));
	}
	
	@Test
	public  final void algorithm2() {
		Assert.assertEquals(9, Algorithm.values().length);
		for(final Algorithm algorithm : Algorithm.values()){
			Assert.assertEquals(algorithm, Algorithm.valueOf(algorithm.name()));
		}
	}
	@Test
	public final void toHexString() {
		final  DigestImpl digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest",TEXT);
		ReflectionTestUtils.setField(digest, "algorithm", Algorithm.MD5);
		digest.toHexString();
		
		Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
	}
	
	@Test
	public final void toHexStringAlreadyDigest() {
		final  DigestImpl digest = new DigestImpl();
		ReflectionTestUtils.setField(digest, "digest", DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase());
		ReflectionTestUtils.setField(digest, "algorithm", Algorithm.MD5);
		ReflectionTestUtils.setField(digest, "crypted", true);
		digest.toHexString();
		Assert.assertEquals( DigestUtils.md5DigestAsHex(TEXT.getBytes()).toUpperCase(), ReflectionTestUtils.getField(digest, "digest"));
	}
	
	@Test
	public final void algorithm() throws NoSuchAlgorithmException {
		final Digest digest = new DigestImpl();
		for(final Algorithm algorithm : Algorithm.values()){
			digest.assignDigest(TEXT, algorithm);
		}
	}
	
	@Test
	public  final void test() {
		final Digest digest = new DigestImpl();
		final Person person = new NaturalPersonImpl("Katy", "Perry", new NativityImpl("Santa Barbara" , new GregorianCalendar(1985, 9, 25).getTime())	);
		person.digest.assignDigest("lesbe", Algorithm.MD5);
		System.out.println(ReflectionTestUtils.getField(digest, "digest"));
		
	}
	

}

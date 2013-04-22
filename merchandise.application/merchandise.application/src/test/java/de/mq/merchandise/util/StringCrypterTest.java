package de.mq.merchandise.util;

import junit.framework.Assert;

import org.junit.Test;

public class StringCrypterTest {
	
	
	private static final String TEXT = "Kylie is nice, hot and kinky";
	private static final String DELIMITER = ":";
	private static final long CUSTOMER_ID = 4711L;
	private static final long USER_ID = 19680528L;
	private StringCrypter stringCrypter = new SimpleStringCrypter();
	
	@Test
	public final void  concatWithCurrentTimeAsFactorFromSeconds() {
		final String result = stringCrypter.concatWithCurrentTimeAsFactorFromSeconds(60, DELIMITER,  USER_ID, CUSTOMER_ID);
		Assert.assertEquals(String.format("%s:%s:%S", USER_ID, CUSTOMER_ID, System.currentTimeMillis()/1000/60), result);
	}

	@Test
	public final void concat() {
		Assert.assertEquals(String.format("%s:%s" ,USER_ID, CUSTOMER_ID), stringCrypter.concat(DELIMITER, USER_ID, CUSTOMER_ID));
	}
	
	@Test
	public final void encryptAndDecrypt() {
		final String key = String.format("%s:%s" ,USER_ID, CUSTOMER_ID);
		Assert.assertEquals(TEXT, stringCrypter.decrypt(stringCrypter.encrypt(TEXT, key), key));
	}
	
	@Test(expected=IllegalStateException.class)
	public final void encryptWrongAlgoritmn(){
		new SimpleStringCrypter("XXX" , "MD5").encrypt(TEXT, String.format("%s:%s" ,USER_ID, CUSTOMER_ID));
	}
	
	@Test(expected=IllegalStateException.class)
	public final void decryptException() {
		final String key = String.format("%s:%s" ,USER_ID, CUSTOMER_ID);
		new SimpleStringCrypter("XXX" , "MD5").decrypt(this.stringCrypter.encrypt(TEXT, key), key);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void differentKeys() {
		stringCrypter.decrypt(stringCrypter.encrypt(TEXT, String.format("%s:%s" ,USER_ID, CUSTOMER_ID)), String.format("%s" ,USER_ID));
	}
}

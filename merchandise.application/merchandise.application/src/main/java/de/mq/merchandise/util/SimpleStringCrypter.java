package de.mq.merchandise.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SimpleStringCrypter {
	static final String MESSAGE_DIGEST_ALGORITHMN = "MD5";
	static final String CRYPT_ALGORITHMN = "AES";
	
	public static long currentTimeAsFactorFromSeconds(final long factor) {
		return System.currentTimeMillis() / 1000 / factor;
	}

	public static String encrypt(final String data, final String keyValue) {
		try {
			return doEncrypt(data, keyValue);
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}

	}

	public static String decrypt(final String encryptedData, final String keyValue) {
		try {
			return doDecrypt(encryptedData, keyValue);
		} catch (final BadPaddingException ex) {
			throw new IllegalArgumentException(ex);
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}

	}

	private static String doEncrypt(final String data, final String keyValue) throws Exception {
		final Cipher c = Cipher.getInstance(CRYPT_ALGORITHMN);
		c.init(Cipher.ENCRYPT_MODE, generateKey(keyValue));
		return new BASE64Encoder().encode(c.doFinal(data.getBytes()));
	}

	private static String doDecrypt(final String encryptedData, final String keyValue) throws IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		final Cipher c = Cipher.getInstance(CRYPT_ALGORITHMN);
		c.init(Cipher.DECRYPT_MODE, generateKey(keyValue));
		return new String(c.doFinal(new BASE64Decoder().decodeBuffer(encryptedData)));

	}

	private static Key generateKey(final String keyValue) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHMN);
		digest.update(keyValue.getBytes());
		final byte[] key = digest.digest();
		return new SecretKeySpec(key, CRYPT_ALGORITHMN);

	}


}

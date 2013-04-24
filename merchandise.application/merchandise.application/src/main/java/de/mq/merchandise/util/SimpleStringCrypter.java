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

public class SimpleStringCrypter implements StringCrypter {
	private final String messageDigestAlgoritm ;
	
	SimpleStringCrypter(final String messageDigestAlgoritm, final String cryptAlgorithm) {
		this.messageDigestAlgoritm = messageDigestAlgoritm;
		this.cryptAlgorithm = cryptAlgorithm;
	}

	private final String cryptAlgorithm ;
	public SimpleStringCrypter() {
		messageDigestAlgoritm = "MD5";
		cryptAlgorithm = "AES";
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.StringCrypter#encrypt(java.lang.String, java.lang.String)
	 */
	@Override
	public  String encrypt(final String data, final String keyValue) {
		try {
			return doEncrypt(data, keyValue);
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}

	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.StringCrypter#decrypt(java.lang.String, java.lang.String)
	 */
	@Override
	public String decrypt(final String encryptedData, final String keyValue) {
		try {
			return doDecrypt(encryptedData, keyValue);
		} catch (final BadPaddingException ex) {
			throw new IllegalArgumentException(ex);
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}

	}

	private  String doEncrypt(final String data, final String keyValue) throws Exception {
		final Cipher c = Cipher.getInstance(cryptAlgorithm);
		c.init(Cipher.ENCRYPT_MODE, generateKey(keyValue));
		return new BASE64Encoder().encode(c.doFinal(data.getBytes()));
	}

	private  String doDecrypt(final String encryptedData, final String keyValue) throws IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		final Cipher c = Cipher.getInstance(cryptAlgorithm);
		c.init(Cipher.DECRYPT_MODE, generateKey(keyValue));
		return new String(c.doFinal(new BASE64Decoder().decodeBuffer(encryptedData)));

	}

	private  Key generateKey(final String keyValue) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance(messageDigestAlgoritm);
		digest.update(keyValue.getBytes());
		final byte[] key = digest.digest();
		return new SecretKeySpec(key, cryptAlgorithm);

	}


}

package de.mq.merchandise.util;
/**
 * Util class to crypt and crypt passwords in security tokens to use them in springsecurity
 * @author manfred.quasten
 *
 */
public interface StringCrypter {

	
	/**
	 * Encrypt the data with the given keyValue. 
	 * The keyValue will be transformed to a 16 byte message digest. The digest is used as key for encrypt.
	 * The result will be base64 encoded.
	 * @param data the data that will be encrypt
	 * @param keyValue the keyValue, used to create the key digest 
	 * @return the base64 encoded key.
	 */
	String encrypt(final String data, final String keyValue);

	/**
	 * Decrypt the base64 data with the given keyValue.  
	 * The keyValue will be transformed to a 16 byte message digest. The digest is used as key for decrypting.
	 * @param encryptedData he data that will be decrypted
	 * @param keyValue´the keyValue, used to create the key digest 
	 * @return the decryped data string
	 */
	String decrypt(final String encryptedData, final String keyValue);

}
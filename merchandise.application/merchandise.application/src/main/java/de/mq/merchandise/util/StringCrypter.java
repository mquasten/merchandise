package de.mq.merchandise.util;
/**
 * Util class to crypt and crypt passwords in security tokens to use them in springsecurity
 * @author manfred.quasten
 *
 */
public interface StringCrypter {

	/**
	 * Builds a key dependend from currentTime and some database ids delimited with the given delimiter. 
	 * This key can be used used as key for encrypt and decrypt. 
	 * @param factor the factor for the time that will be used 1 is seconds, 60 minutes from beginning of unix age.
	 * @param del the used delimiter
	 * @param ids the ids that are concatinated to the key
	 * @return the concatination of the ids and the time with the given delimiter.
	 */
	String concatWithCurrentTimeAsFactorFromSeconds(final long factor, final String del, final long... ids);

	/**
	 * Concat the given ids with the given delimiter. The Result can be used as key.
	 * @param del the used delimiter
	 * @param ids the ids that are concatinated to the key
	 * @return the concatination of the ids and the time with the given delimiter.
	 */
	String concat(final String del, final long... ids);

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
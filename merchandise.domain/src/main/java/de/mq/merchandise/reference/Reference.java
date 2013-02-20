package de.mq.merchandise.reference;

import java.io.Serializable;


/**
 * Key for translations can be selected by its referenceTypes. 
 * For example DE, EN are keys for the referenceType Languge
 * @author MQuasten
 *
 */
public interface Reference extends Serializable{

	/**
	 * The type of the reference. Enum for referencetype
	 * @author Admin
	 *
	 */
	enum Kind {
		Country,
		Language;
	}
	
	/**
	 * The key that can be translated
	 * @return the key for translation i18n
	 */
	String key();

	/**
	 * The group to that the key belongs to.
	 * @return the group of the key
	 */
	Kind referenceType();

}
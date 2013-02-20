package de.mq.merchandise.contact;


/**
 * A PostBoxAddress: City, zipCode and box
 * @author ManfredQuasten
 *
 */
public interface PostBox extends CityAddress {
	
	/**
	 * The number of the boxBox
	 * @return boxBox code
	 */
	String box();
	

}

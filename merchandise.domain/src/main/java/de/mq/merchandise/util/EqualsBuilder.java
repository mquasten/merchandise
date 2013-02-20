package de.mq.merchandise.util;

/**
 * Checks if 2 objects are equals
 * The fields that should be used for the heck must be annotated with equals 
 * @author ManfredQuasten
 *
 */
public interface EqualsBuilder {

	/**
	 * Specifies the first (source) object for the equals check
	 * @param source the source object for the equals check
	 * @return the builder it self
	 */
	EqualsBuilder withSource(final Object source);
	
	/**
	 * Specifies the second (target) object for the equals check
	 * @param target the target object for the equals check
	 * @return the builder it self
	 */
	EqualsBuilder withTarget(final Object source);
	
	/**
	 * Specifies the class from which the target object must be an instance.
	 * If it isn't instance of that the equals check returns false
	 * @param clazz the class from which the target object must be an instance that the equlas check can return true
	 * @return  the builder it self
	 */
	EqualsBuilder forInstance(Class<?> clazz);

	/**
	 * The equals check. The equals is a composition from all annotated fields.
	 * If one field is null, the parent equals  is called
	 * @return true if the 2 objects are equals, else false
	 */
	boolean isEquals();
	
	/**
	 * Builds the hashcode of the source object
	 * The hashcode is a composition of all annotated fields.
	 * If one of the fields is null, the hashCode from the parent object is returned
	 * @return the hashCode of the source object
	 */
	int buildHashCode();

	

}
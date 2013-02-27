package de.mq.merchandise.controller;

/**
 * Get the localized message.
 * The locale from user object is used to localize it.
 * @author Admin
 *
 */
public interface MessageSouceController {

	String get(Object key);

}
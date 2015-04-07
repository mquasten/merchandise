package de.mq.merchandise.support;

import java.util.List;

import de.mq.merchandise.domain.subject.support.Subject;
/**
 * Der Kunde.
 * @author Admin
 *
 */
public interface Customer extends BasicEntity {

	/**
	 * Name des Kunden
	 * @return der KundenName
	 */
	String name();
	
	/**
	 * Ein Kunde kann eine Belibige Anzahl von Subjects haben, deren Eigentuemer er ist
	 * @return die Subjects des Kunden
	 */
	List<Subject> subjects();

}
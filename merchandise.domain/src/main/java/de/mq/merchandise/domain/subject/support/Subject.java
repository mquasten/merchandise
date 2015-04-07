package de.mq.merchandise.domain.subject.support;

import de.mq.merchandise.support.BasicEntity;
import de.mq.merchandise.support.Customer;

/**
 * Eine subject kann ein Artikel (Bestellposition)  in einer Bestellung oder eine Item einer Ausschreibung  oder eine Postion fuer eine Versteigerung sein
 * @author Admin
 *
 */
public interface Subject  extends  BasicEntity {

	/**
	 * Der Name des Subjektes
	 * @return der Name
	 */
	String name();

	/**
	 * Die Beschreibung des Subjects
	 * @return die Beschreibung
	 */
	String description();

	/**
	 * Der Eigentuemer des Subjects, der Owner.
	 * @return der Eigentuemer
	 */
	Customer customer();

}
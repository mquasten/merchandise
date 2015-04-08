package de.mq.merchandise.domain.subject.support;

import java.util.Collection;

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

	/**
	 * Erzeugt eine Condition und fuegt sie dem Subject hinzu.
	 * Wenn eine Condition des ConditionType schon existiert, wird keine weitere hinzugefuegt.
	 * Es kann max. eine Condition des selben Contitiontyp innerhalb eines Subjects existieren.
	 * @param conditionType die Art der Condition, bestimmt die damit verbundene Verarbeitung durch Algorithmen. 
	 * @param datatype der DatenTyp der Werte fuer die Condition
	 */
	void add(final String conditionType, final ConditionDataType datatype);

	/**
	 * Loescht die Condition des ConditionTypes vom Subject.
	 * Existiert keine Condition, geschieht nichts (idempotent)
	 * @param conditionType die Art der Condition
	 */
	void remove(final String conditionType);

	/**
	 * Alle Conditions am Subject
	 * @return alle Conditions des Subjects
	 */
	Collection<Condition> conditions();

}
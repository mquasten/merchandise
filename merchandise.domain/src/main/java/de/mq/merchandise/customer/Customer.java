package de.mq.merchandise.customer;

import java.util.Collection;
import java.util.List;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.BasicEntity;
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

	/**
	 * Gibt ConditionTypen  zurueck, die benutzt werden koenne, um Conditions fuer Subjects zu definieren.
	 * @return Collection aller erlaubten ConditionTypes
	 */
	Collection<String> conditionTypes();

	/**
	 * Fuegt einen Conditiontyp hinzu
	 * @param conditiontype neuer ConditionTyp, der benutzt werden soll
	 */
	void assignConditionType(final String conditiontype);

	/**
	 * Entfernt einen ConditionType. Dieser kann dann nicht mehr benutzt werden. 
	 * Auf bereits verwendete Typen hat das keinen Einfluss, das keine Datenbankreferenzen verwendet werden, sondern nur Strings.
	 * @param conditiontype. ConditionTyp, der entfernt werden soll.
	 */
	void removeConditionType(final String conditiontype);

}
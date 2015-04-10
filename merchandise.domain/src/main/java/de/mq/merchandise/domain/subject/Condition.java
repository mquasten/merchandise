package de.mq.merchandise.domain.subject;

import java.util.List;

import de.mq.merchandise.domain.subject.support.ConditionDataType;
import de.mq.merchandise.support.BasicEntity;

/**
 * Eine Condition definiert, welche Werte ein Subject haben muss, bzw. welche Werte als (User) Input erwartet werden. 
 * Eine Liste steht fuer eine Werteauswahl, ein Wert fuer eine Konstante und eine leere Liste fuer eine Benutzereingabe.
 * @author Admin
 *
 * @param <T> der Typ der Condition, muss corespondieren mit {@link ConditionDataType}
 */
public interface Condition<T>  extends BasicEntity{

	/**
	 * Einen Wert hinzufuegen.
	 * @param value der hinzuzufuegende Wert.
	 */
	void add(final T value);

	/**
	 * Einen Wert loeschen
	 * @param value der zu loeschende Wert
	 */
	void remove(final T value);

	/**
	 * Liste der Werte (nicht modifizierbar).
	 * @return Liste der moeglichen Werte.
	 */
	List<T> values();

	/**
	 * Alle Werten loescchen.
	 */
	void clear();

}

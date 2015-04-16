package de.mq.merchandise.subject;

import java.util.List;

import de.mq.merchandise.domain.support.BasicEntity;
import de.mq.merchandise.subject.support.ConditionDataType;

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
	
	/**
	 * Subject, zu dem die Condition zugeordnet ist.
	 * @return Subject der Condition
	 */
	Subject subject();
	/**
	 * Type der Condition
	 * @return Type der Condition
	 */
	String conditionType();

	/**
	 * DatenTyp der Condition
	 * @return der DatenTyp der Condition
	 */
	ConditionDataType conditionDataType(); 

}

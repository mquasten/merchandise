package de.mq.merchandise.subject;

import de.mq.merchandise.subject.support.ConditionDataType;
import de.mq.merchandise.support.BasicEntity;

/**
 * Eine Condition definiert, welche Werte ein Subject haben muss, bzw. welche Werte als (User) Input erwartet werden. 
 * Eine Liste steht fuer eine Werteauswahl, ein Wert fuer eine Konstante und eine leere Liste fuer eine Benutzereingabe.
 * @author Admin
 *
 * @param <T> der Typ der Condition, muss corespondieren mit {@link ConditionDataType}
 */
public interface Condition<T>  extends BasicEntity {

	
	
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

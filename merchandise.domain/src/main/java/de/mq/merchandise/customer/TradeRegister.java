package de.mq.merchandise.customer;

import java.io.Serializable;
import java.util.Date;

public interface TradeRegister extends Serializable {

	String zipCode();

	String city();

	String reference();

	int hashCode();

	boolean equals(Object obj);

	String toString();

}
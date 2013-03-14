package de.mq.merchandise.customer;

import java.io.Serializable;
import java.util.Date;

public interface Nativity extends Serializable {

	public abstract String birthPlace();

	public abstract Date birthDate();

}
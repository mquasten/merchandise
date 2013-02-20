package de.mq.merchandise.contact;

import java.io.Serializable;

public interface Coordinates extends Serializable{

	double longitude();

	double latitude();

	double distance(final Coordinates coordinates);

}
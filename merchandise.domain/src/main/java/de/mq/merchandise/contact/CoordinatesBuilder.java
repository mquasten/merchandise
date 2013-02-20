package de.mq.merchandise.contact;


public interface CoordinatesBuilder {

	CoordinatesBuilder withLongitude(double longitude);

	CoordinatesBuilder withLatitude(double latitude);

	Coordinates build();

}
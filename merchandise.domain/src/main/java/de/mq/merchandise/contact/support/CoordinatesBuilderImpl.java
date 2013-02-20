package de.mq.merchandise.contact.support;

import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.CoordinatesBuilder;
import de.mq.merchandise.util.EntityUtil;

class CoordinatesBuilderImpl implements CoordinatesBuilder {
	
	private Double longitude;
	
	private Double latitude;
	
	
	@Override
	public final CoordinatesBuilder withLongitude(final double longitude) {
		this.longitude=longitude;
		return this;
	}
	
	
	@Override
	public final CoordinatesBuilder withLatitude(final double latitude) {
		this.latitude=latitude;
		return this;
	}
	
	
	@Override
	public Coordinates build() {
		EntityUtil.notNullGuard(latitude, "latitude");
		EntityUtil.notNullGuard(longitude, "longitude");
		return new CoordinatesImpl(latitude, longitude);
	}

}

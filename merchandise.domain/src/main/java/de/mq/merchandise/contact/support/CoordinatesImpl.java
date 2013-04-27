package de.mq.merchandise.contact.support;

import javax.persistence.Embeddable;

import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Embeddable
class CoordinatesImpl implements Coordinates {
	
	static final double RE = 6371;
	
	private static final long serialVersionUID = 1L;

	@Equals
	private final Double longitude;
	@Equals
	private final Double latitude;

	@SuppressWarnings("unused")
	private CoordinatesImpl() {
		this.latitude=null;
		this.longitude=null;
	}
	
	public CoordinatesImpl(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	
	@Override
	public   double longitude() {
		EntityUtil.notNullGuard(longitude, "longitude");
		return longitude;
	}

	
	@Override
	public  double latitude() {
		EntityUtil.notNullGuard(latitude, "latitude");
		return latitude;
	}
	
	@Override
	public  double distance(final Coordinates coordinates) {
		return RE* Math.acos(Math.sin(coordinates.latitude()*Math.PI/180)* Math.sin(latitude*Math.PI/180) + Math.cos(coordinates.latitude()*Math.PI/180)*Math.cos(latitude*Math.PI/180) * Math.cos((longitude*Math.PI/180)-(coordinates.longitude()*Math.PI/180)));
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	
	
	

}

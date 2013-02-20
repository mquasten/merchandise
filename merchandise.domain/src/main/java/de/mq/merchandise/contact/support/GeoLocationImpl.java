package de.mq.merchandise.contact.support;

import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="GeoLocation")
@Table(name="geo_location")
@NamedQuery(name=GeoLocationImpl.QUERY_CITY_BY_NAME_PATTERN, query="select c from GeoLocation c where c.city_upper like upper(:city) and c.country like  upper(:country) and c.place is null and  c.id =  (select min(id) from GeoLocation x  where c.id = x.id )" )
public class GeoLocationImpl  implements CityAddress {
	
	private static final long serialVersionUID = 1L;

	public static final String QUERY_CITY_BY_NAME_PATTERN = "CityByNamePattern";

	@Id
	@Equals
	private Long id;
	
	@Column(length=50,insertable=false, updatable=false, nullable=false)
	private String city;
	
	@Column(length=10, name="zip_code", insertable=false, updatable=false, nullable=false)
	private String zipCode;
	
	@Column(length=2,insertable=false, updatable=false, nullable=false)
	private String country;
	
	@Column(length=50,insertable=false, updatable=false, nullable=false)
	private String city_upper;
	
	
	@Column(length=50, insertable=false, updatable=false)
	private String place;
	
	@Embedded()
	@Basic(optional=false)
	@AttributeOverrides( {
	@AttributeOverride(column=@Column(name="longitude",insertable=false, updatable=false), name = "longitude"),
	@AttributeOverride(column=@Column(name="latitude",insertable=false, updatable=false), name = "latitude")
	})
	private Coordinates coordinates;
	
	private GeoLocationImpl() {
		
	}

	@Override
	public String contact() {
		 return place()+ " " + zipCode() +" " + city() + " " + country().getCountry();
	}

	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}

	@Override
	public String zipCode() {
		return zipCode;
	}

	@Override
	public String city() {
		return  city;
	}

	@Override
	public Locale country() {
		return new Locale(country, country);
	}
	
	public final String place() {
		return place;
	}
	
	public final Coordinates coordinates() {
		return coordinates;
		
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}

	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	

}

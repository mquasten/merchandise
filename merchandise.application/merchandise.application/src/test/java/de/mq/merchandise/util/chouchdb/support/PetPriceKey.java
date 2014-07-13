package de.mq.merchandise.util.chouchdb.support;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import de.mq.merchandise.util.chouchdb.Field;

@JsonPropertyOrder(value={"quality", "unit"})
class PetPriceKey {
	
	@JsonProperty("unit")
	@Field
	String unit;
	@JsonProperty("quality")
	@Field
	String  quality;
	
	

	public PetPriceKey(String quality, String unit) {
	
		this.quality = quality;
		this.unit = unit;
	}
	
	@SuppressWarnings("unused")
	private PetPriceKey() {
		
	}

	
}
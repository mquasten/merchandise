package de.mq.merchandise.order.support;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder(value={"quality", "unit"})
class PetPriceKey {
	
	@JsonProperty("unit")
	String unit;
	@JsonProperty("quality")
	String  quality;
	
	

	public PetPriceKey(String quality, String unit) {
	
		this.quality = quality;
		this.unit = unit;
	}
	
	@SuppressWarnings("unused")
	private PetPriceKey() {
		
	}

	
}
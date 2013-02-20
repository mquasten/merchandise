package de.mq.merchandise.contact.support;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.mq.merchandise.util.Equals;



@Entity(name="PhoneContact")
class PhoneContactImpl  extends AbstractContact {

	private static final long serialVersionUID = 1L;

	static final String AREA_CODE_DE = "49";

	@Equals
	@Column(name="area_code", length=10)
	private final String areaCode;
	
	@Equals
	@Column(name=" international_area_code", length=3)
	private final String internationalAreaCode;


	PhoneContactImpl(final String internationalAreaCode,final String areaCode, final String callNumber, final boolean isLogin) {
		super(callNumber,isLogin);
		this.areaCode = areaCode;
		this.internationalAreaCode = internationalAreaCode;
	}
	
	@SuppressWarnings("unused")
	private PhoneContactImpl() {
		this(null,null,null,false);
	}


	@Override
	public String contactInfo() {
		return this.internationalAreaCode + this.areaCode + this.account;
	}
	
	
	

}

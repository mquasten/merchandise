package de.mq.merchandise.contact.support;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.util.Equals;

@Entity(name="PostAddress")
class PostBoxImpl extends AbstractCityAddress implements PostBox {

	
	private static final long serialVersionUID = 1L;
	@Column(name="post_box", length=10)
	@Equals
	private final String postBox; 
	
	@SuppressWarnings("unused")
	private PostBoxImpl() {
		super(new Locale("",""), null,null);
		this.postBox=null;
	}
	
	
	PostBoxImpl(final Locale country, final String zipCode, final String city, final String postBox){
		super(country, zipCode,city);
		this.postBox=postBox;
	}
	
	@Override
	public String box() {
		return postBox;
	}

	


	@Override
	protected  String contactInfo() {
		return  postBox+ ", " + zipCode() +" " + city() + " " + country().getCountry();
	}

}

package de.mq.merchandise.customer.support;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class DigestAO  implements Serializable {
	
	
		private static final long serialVersionUID = 1L;

		@Size(min=4, max=8 , message="{wrong_password}")
	    @Getter(clazz=DigestImpl.class, value = "digest")
		public abstract String getDigest(); 

	    @Setter(clazz=DigestImpl.class, value = "digest")
		public abstract void setDigest(String digest);
	    
	    @Size(min=4, max=8 , message="{wrong_password}")
		@Getter(value="confirmedDigest")
		public abstract String getConfirmedDigest();
		
		@Setter(value="confirmedDigest")
		public abstract void setConfirmedDigest(final String password);
		
		
		

		
		

}

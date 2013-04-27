package de.mq.merchandise.customer.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Embeddable
public class DigestImpl implements Digest {
	
	private static final long serialVersionUID = 1L;

	@Column(length=10)
	@Basic(optional=false)
	@Equals
	@Enumerated(EnumType.STRING)
	private Digest.Algorithm algorithm=Algorithm.MD5;
	
	@Column(length=50)
	@Basic(optional=false)
	@Equals
	private String digest;
	
	@Column()
	@Basic(optional=false)
	private boolean crypted=false;
	
	DigestImpl() {
		
	}
	
	DigestImpl(final String text, final Algorithm algorithm) {
		assignDigest(text,algorithm);
	}
	
	@Override
	public void   assignDigest(final String text, final Algorithm algorithm) {
		EntityUtil.notNullGuard(algorithm, "algorithm");
		EntityUtil.mandatoryGuard(text, "text");
		this.algorithm=algorithm;
		if( algorithm == Algorithm.UNCRYPTED){
			digest=text;
			crypted=true;
			return;
		}
		
		if( algorithm== Algorithm.NON){
			digest=hex(text.getBytes());
			crypted=true;
			return;
		}
		digest=digestAsHex(text, algorithm.algorithm());
	}
	
	@Override
	public void   assignDigest(final String text) {
		assignDigest(text, Algorithm.MD5);
	}

	String digestAsHex(final String text, final String algorithm)  {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(text.getBytes());
			this.crypted=true;
		    return hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException ex) {
			
			throw new IllegalArgumentException(ex);
		}
	}

	private String hex(final byte[] text) {
		final StringBuffer buffer = new StringBuffer();
		for(byte b : text ){
			buffer.append(toHexString(b));
		}
		return buffer.toString();
	}
	
    private String toHexString(byte b ) {
		return ( ( b & 0x7F) + (b < 0 ? 128:0) < 16 ? "0" : "" )  + Integer.toHexString(( b & 0x7F) + (b < 0 ? 128:0)).toUpperCase();
		
	}

	@Override
	public boolean check(final String text) {
		EntityUtil.mandatoryGuard(text, "text");
		EntityUtil.mandatoryGuard(digest, "digest");
		
		if(! crypted ) {
			
			return digest.equals(text);
		}
		EntityUtil.notNullGuard(algorithm, "algorithm");
		if( algorithm == Algorithm.UNCRYPTED) {
			return digest.equals(text);
		}
		
		return this.digest.equalsIgnoreCase(digestAsHex(text, algorithm.name()));
	}
	
	
	void toHexString() {
		EntityUtil.notNullGuard(digest, "digest");
		if( crypted) {
		    return; 	
		}
		assignDigest(digest, algorithm);
		
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	

}

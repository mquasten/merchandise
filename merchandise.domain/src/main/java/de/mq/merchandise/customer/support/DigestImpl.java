package de.mq.merchandise.customer.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.mq.merchandise.util.EntityUtil;

@Embeddable
public class DigestImpl implements Digest {
	
	@Column(length=3)
	@Basic(optional=false)
	private Digest.Algorithm algorithm=Algorithm.MD5;
	
	@Column(length=50)
	@Basic(optional=false)
	private String digest;
	
	@Column()
	@Basic(optional=false)
	private boolean crypted=false;
	
	DigestImpl() {
		
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

	final String digestAsHex(final String text, final String algorithm)  {
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
	
	

}

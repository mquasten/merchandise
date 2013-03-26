package de.mq.merchandise.customer.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.mq.merchandise.util.EntityUtil;

@Embeddable
public class DigestImpl implements Digest {
	
	@Column(length=3)
	private Digest.Algorithm algorithm;
	
	@Column(length=50)
	private String digest;
	
	
	@Override
	public void   assignDigest(final String text, final Algorithm algorithm) {
		EntityUtil.notNullGuard(algorithm, "algorithm");
		EntityUtil.mandatoryGuard(text, "text");
		this.algorithm=algorithm;
		if( algorithm== Algorithm.NON){
			digest=hex(text.getBytes());
			return;
		}
		digest=digestAsHex(text, algorithm.name());
	}
	
	@Override
	public void   assignDigest(final String text) {
		assignDigest(text, Algorithm.MD5);
	}

	final String digestAsHex(final String text, final String algorithm)  {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(text.getBytes());
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
		EntityUtil.notNullGuard(algorithm, "algorithm");
		return this.digest.equalsIgnoreCase(digestAsHex(text, algorithm.name()));
	}
	
	
	
	

}

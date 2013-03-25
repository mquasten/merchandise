package de.mq.merchandise.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final   class DigestUtil {
	
	public enum  Algorithm {
		SHA,
		MD5;
		
		
	}
	
	public static String digestAsHex(final String text, final Algorithm algorithm) {
		return digestAsHex(text, algorithm.name());
		
	}

	static String digestAsHex(final String text, final String algorithm)  {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(text.getBytes());
		    final StringBuffer buffer = new StringBuffer();
			for(byte b : messageDigest.digest() ){
				buffer.append(toHexString(b));
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException ex) {
			
			throw new IllegalArgumentException(ex);
		}
	}
	
	static  String toHexString(byte b ) {
		return ( ( b & 0x7F) + (b < 0 ? 128:0) < 16 ? "0" : "" )  + Integer.toHexString(( b & 0x7F) + (b < 0 ? 128:0)).toUpperCase();
		
	}
	
	

}

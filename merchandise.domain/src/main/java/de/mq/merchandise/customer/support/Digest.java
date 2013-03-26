package de.mq.merchandise.customer.support;

public interface Digest {

	enum  Algorithm {
		SHA,
		MD5,
    	NON;
	}
	
	void assignDigest(final String text, final Algorithm algorithm);

	void assignDigest(String text); 
	
	boolean check(final String text);
	

}
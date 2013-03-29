package de.mq.merchandise.customer.support;

import java.io.Serializable;

public interface Digest extends Serializable{

	enum  Algorithm {
		SHA_1("SHA-1"),    //Produces 20-byte digests; suitable for documents of less than 264 bits; recently compromised.

		SHA("SHA"), // In Java, this is an alias for SHA-1. In other contexts, it refers to SHA-0, a compromised and withdrawn standard Java has never supported. It sometimes also refers to the whole family of Secure Hash Algorithms as defined in Secure Hash Standard, NIST FIPS 180-2 Secure Hash Standard (SHS); http://csrc.nist.gov/publications/fips/fips180-2/fips180-2.pdf.

		SHA_256("SHA-256"), //Produces 32-byte digests; suitable for documents of less than 264 bits.

		SHA_384("SHA-384"), //Produces 48-byte digests; suitable for documents of less than 2128bits.

		SHA_512("SHA-512"), //Produces 64-byte digests; suitable for documents of less than 2128bits.

		MD2("MD2"),  //RSA Message Digest 2 as defined in RFC 1319 and RFC 1423 (RFC 1423 corrects a mistake in RFC 1319); produces 16-byte digests; suitable for use with digital signatures.

		MD5("MD5"), //RSA Message Digest 5 as defined in RFC 1321; produces 16-byte digests; quite fast on 32-bit machines.
		
		NON("---"), // The the characters as hexCodes
		UNCRYPTED("---"); // the characters in readable form 
		
		private final  String algorithm; 
		
		Algorithm (final String algorithm) {
			this.algorithm=algorithm;
		}
		
		public final String algorithm(){
			return this.algorithm;
		}
	}
	
	void assignDigest(final String text, final Algorithm algorithm);

	void assignDigest(String text); 
	
	boolean check(final String text);
	

}
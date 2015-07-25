package de.mq.merchandise.support;

public interface Mapper<Source,Target> {
	
	Target mapInto(final Source source, final Target target );
	

}

package de.mq.merchandise.util.support;

import javax.persistence.Embeddable;

import de.mq.merchandise.BasicEntity;

@Embeddable
class MockImpl implements BasicEntity {

	
	private static final long serialVersionUID = 1L;

	@Override
	public long id() {
		
		return 19680528L;
	}

	@Override
	public boolean hasId() {
		return true;
	}
	
}

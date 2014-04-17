package de.mq.merchandise.util.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.mq.merchandise.BasicEntity;
/**
 * A simple TestEntity to Simmulate Class selection without any impact of the real persistence provider
 * @author mquasten
 *
 */
@TestEntity
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
/**
 * A Test Annotation, that isn't detected by persistence provider
 * @author Admin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface TestEntity {
	
}



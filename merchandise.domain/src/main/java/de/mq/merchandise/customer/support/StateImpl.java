package de.mq.merchandise.customer.support;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.mq.merchandise.customer.State;

/**
 * The state of of customer, user.
 * It can activated or deActivated.
 * Activate and deactivate methods  are implemented  idempotent
 * @author ManfredQuasten
 *
 */

@Embeddable()
public class StateImpl implements State {
	
	
	private static final long serialVersionUID = 1L;
	@Column(name="active", nullable=false)
	private boolean active=false;
	
	StateImpl() {
		active=false;
	}
	
	StateImpl(final boolean active) {
		this.active=active;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.StateAware#isActive()
	 */
	public boolean isActive() {
		return active;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.StateAware#activate()
	 */
	public  void activate() {
		this.active=true;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.StateAware#deActivate()
	 */
	public void deActivate() {
		this.active=false;
	}

}

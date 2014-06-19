package de.mq.merchandise.order.support;

import java.util.Collection;
import java.util.Collections;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

public class ItemSetImpl implements ItemSet  {


	@Equals
	private final Opportunity opportunity;
	
	@Equals
	private final Customer tradingPartner;
	
	private Date submitted; 
	
	@Equals
	private final Date created = new Date(); 
	
	private Map<String,Item> items = new HashMap<>();
	
	
	public ItemSetImpl(final Customer tradingPartner, final Opportunity opportunity) {
		this.tradingPartner = tradingPartner;
		this.opportunity = opportunity;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#submitted()
	 */
	@Override
	public final Date submitted() {
		EntityUtil.notNullGuard(submitted, "SubmitDate");
		return submitted;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#submitted()
	 */
	@Override
	public final Date created() {
		EntityUtil.notNullGuard(created, "CreatedDate");
		return created;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#isSubmitted()
	 */
	@Override
	public boolean  isSubmitted() {
		return (submitted != null);
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#tradingPartner()
	 */
	@Override
	public Customer tradingPartner() {
		return tradingPartner;
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#opportunity()
	 */
	@Override
	public Opportunity opportunity() {
		return opportunity;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#amount()
	 */
	@Override
	public Money amount() {
		if(items.isEmpty()){
			throw new IllegalArgumentException("No items aware, amount can't be calculated");
		}
		
		Money amount = new MoneyImpl(0, items.values().iterator().next().amount().currency());
		for(final Item item : items.values()){
			amount=amount.add(item.amount());
		}
		return amount;
	}
		

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#assign(java.lang.String)
	 */
	@Override
	public void assign(final Item item) {
		if(items.containsKey(item.itemId())){
			throw new IllegalArgumentException("Item for " + item.itemId() + "already exists.");
		}
		items.put(item.itemId(), item);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#remove(java.lang.String)
	 */
	@Override
	public void remove(final String itemId) {
		items.remove(itemId);
	}
	
	@Override
	public void remove(final Item item) {
		items.remove(item.itemId());
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#item(java.lang.String)
	 */
	@Override
	public Item item(final String itemId) {
		if(! items.containsKey(itemId)){
			throw new IllegalArgumentException("Item for id " + itemId + "not assigned.");
		}
		return items.get(itemId);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#items()
	 */
	@Override
	public Collection<Item> items() {
		return Collections.unmodifiableSet(new HashSet<>(items.values()));
	}
	
	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(ItemSet.class).isEquals();
	}
}
